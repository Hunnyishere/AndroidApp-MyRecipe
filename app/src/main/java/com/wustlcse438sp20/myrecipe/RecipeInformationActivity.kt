package com.wustlcse438sp20.myrecipe

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.Adapter.IngredientAdapter
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.*
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import com.wustlcse438sp20.myrecipe.data.Collection
import kotlinx.android.synthetic.main.activity_recipe_information.*
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap



class RecipeInformationActivity : AppCompatActivity() {

    lateinit var recipeviewModel: RecipeViewModel
    lateinit var recyclerview:RecyclerView
    lateinit var adapter:IngredientAdapter
    var IngredientList:ArrayList<ExtendedIngredients> = ArrayList()
    private lateinit var recipe:RecipeInformation
    private lateinit var collectionIds: List<String>
    private var collectionInfos: ArrayList<Collection> = ArrayList()
    private lateinit var db : FirebaseFirestore
    private var recipe_id:Int = 0
    private var type:String = "search"
    private var user_email = ""
    private var selectDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = applicationContext as MyApplication
        user_email = globalVariable.getEmail()!!
        setContentView(R.layout.activity_recipe_information)
        val bundle = intent.extras
        recipe_id = bundle!!.getInt("recipeId")
        type = bundle!!.getString("type").toString()
        if (type.equals("search")){
            recipe_notes_layout.visibility = View.GONE
        }
        //var user_email = bundle!!.getString("user_email")
        Log.v("recipeId get",recipe_id.toString())
        //RecyclerView Adapter
        recyclerview = ingredient_recyclerview
        adapter = IngredientAdapter(this,IngredientList)
        recyclerview.layoutManager = GridLayoutManager(this,4)
        recyclerview.adapter = adapter

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings


        //viewmodel
        recipeviewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        // recipe details
        recipeviewModel.searchRecipeInformation(recipe_id)
        recipeviewModel.recipeDetail.observe(this, Observer {
            if (it.image !== null)
                Picasso.get().load(it.image).into(detail_image)
            textView_cost.text = it.pricePerServing.toString()+" per"
            textView_likes.text = it.aggregateLikes.toString()+" likes"
            textView_readyTime.text = it.readyInMinutes.toString()+" Mins"
            instruction_text.setText( Html.fromHtml(it.instructions))
            IngredientList.clear()
            IngredientList.addAll(it.extendedIngredients)

            recipe = it
            adapter.notifyDataSetChanged()
        })
        // similar recipes
        recipeviewModel.searchSimilarRecipes(recipe_id)
        recipeviewModel.similarRecipes.observe(this, Observer { similarRecipes ->
            textView_similar.text = similarRecipes.joinToString(separator = ", ",
                prefix = "",
                postfix = "",
                limit = 3,
                truncated = "there’s more ..."){it -> "${it.title}"}
//            for(recipeS in similarRecipes){
//                Log.v("id",recipeS.id.toString())
//                Log.v("title",recipeS.image)
//                Log.v("image",recipeS.image)
//                Log.v("imageUrls",recipeS.imageUrls.toString())
//                Log.v("readyInMinutes",recipeS.readyInMinutes.toString())
//                Log.v("servings",recipeS.servings.toString())
//            }

        })

        // search for all collections in userProfile database
        collectionInfos.clear()
        db.collection("collections")
            .whereEqualTo("email", user_email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    collectionInfos.add(Collection(id=document.id,email=document.get("email").toString(),name=document.get("name").toString(),description = document.get("description").toString(),recipes = document.get("recipes") as ArrayList<RecipeShownFormat>))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

        //take pic
        recipe_add_image_button.setOnClickListener(){
            val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),1)
            }else{
                val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cIntent,1)
            }

        }

        //add to meal plan
        add_to_mealplan.setOnClickListener(){
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Add to a collection")
            var dates = arrayOfNulls<CharSequence>(7)
            var selectedDates: ArrayList<CharSequence> = ArrayList()
            val calenders = Calendar.getInstance()
            calenders.add(Calendar.DAY_OF_MONTH, -1)
            dates[0] =parseMonth(calenders.get(Calendar.MONTH)) + " " +calenders.get(Calendar.DAY_OF_MONTH)+ ","+calenders.get(Calendar.YEAR)
            selectDate =dates[0].toString()
            for (i in 1..6) {
                calenders.add(Calendar.DAY_OF_MONTH, +1)
                dates[i] =parseMonth(calenders.get(Calendar.MONTH)) + " " +calenders.get(Calendar.DAY_OF_MONTH)+ ","+calenders.get(Calendar.YEAR)
            }
            builder.setSingleChoiceItems(dates,0, DialogInterface.OnClickListener { dialog, which ->
                Log.v("choose collection", which.toString())
                selectDate = dates[which].toString()
            })
            builder.setPositiveButton("YES",
                DialogInterface.OnClickListener { dialog, which ->
                    AddToMealPlan(selectDate)
                })
            builder.setNegativeButton("NO",DialogInterface.OnClickListener({dialog,which ->
            }))
            val  dialog: AlertDialog= builder.create()
            dialog.show()
        }


        //add to collection
        add_to_collection.setOnClickListener(){
            // 在数据库查询之前加载了
            Log.v("bbbb","跑到这里")
            // pop up alertdialog for user to choose from
            if (collectionInfos.size == 0){
                Log.v("cccc","跑到这里")
                Toast.makeText(this,"You have to create a new collection first" ,Toast.LENGTH_LONG).show()
            }else{
                Log.v("dddd","跑到这里")
                // Dialog  choose playlist
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                var collectionItems = arrayOfNulls<CharSequence>(collectionInfos.size)
                var selectedCollections: ArrayList<Collection> = ArrayList()
                Log.v("listsize:",collectionItems.size.toString())
                for (i in 0..collectionInfos.size-1){
                    Log.v("which",i.toString())
                    collectionItems[i]=collectionInfos[i].name
                }
                builder.setTitle("Add to a collection")
                builder.setMultiChoiceItems(collectionItems, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        Log.v("choose collection", which.toString())
                        selectedCollections.add(collectionInfos[which])
                    })
                builder.setPositiveButton("YES",
                    DialogInterface.OnClickListener { dialog, which ->
                        for (selectedCollection in selectedCollections){
                            var collection_recipes: ArrayList<RecipeShownFormat> = ArrayList()

                            // update recipe into collection(s)
                            db.collection("collections")
                                .whereEqualTo(FieldPath.documentId(), selectedCollection.id)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        Log.d("TAG", "${document.id} => ${document.data}")
                                        var collection_recipes = document.get("recipes") as ArrayList<MutableMap<String,Any>>
                                        Log.v("recipes type",collection_recipes.toString())
                                        val new_recipe:MutableMap<String,Any> = HashMap()
                                        new_recipe["id"] = recipe.id
                                        new_recipe["title"] = recipe.title
                                        new_recipe["image"] = recipe.image!!
                                        collection_recipes.add(new_recipe)
                                        Log.v("recipes type",collection_recipes.toString())

                                        //firebase database can only be updated by map
                                        val updateMap: MutableMap<String, Any> = HashMap()
                                        updateMap.put("recipes", collection_recipes)

                                        document.reference
                                            .update(updateMap)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "Database Update",
                                                    "recipe in collection: DocumentSnapshot successfully updated!"
                                                )
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    "Database Update",
                                                    "recipe in collection: Error updating document",
                                                    e
                                                )
                                            }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("TAG", "Error getting documents: ", exception)
                                }
                        }
                        Toast.makeText(this,"You have added collection: " + recipe.title + " to collections" ,Toast.LENGTH_LONG).show()
                    })
                builder.setNegativeButton("NO", DialogInterface.OnClickListener({ dialog, which ->
                }))
                val  dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }//listener

    }

    override fun onStart() {
        super.onStart()

        // load user note image
        loadNoteImage()
    }

    fun loadNoteImage(){
        // load recipe note image from firebase storage
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child(recipe_id.toString()+"_note.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            // Data for "images/user_email_profile.jpg" is returned, use this as needed
            var bitmap:Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size);
            Log.v("成功加载 bitmap",bitmap.toString())
            recipe_user_image.setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
            Log.v("加载失败 bitmap","")
        }
    }

    fun AddToMealPlan(date:String){
        recipeviewModel.getRecipeNutritionById(recipe.id){
            val data= mapOf<String,Any>(
                "img" to recipe.image!!,
                "title" to recipe.title,
                "recipeId" to recipe.id,
                "calories" to it.calories.toLong(),
                "carbs" to it.carbs.dropLast(1).toLong(),
                "fat" to it.fat.dropLast(1).toLong(),
                "protein" to it.protein.dropLast(1).toLong()
            )
            //数据库操作在这里写
            val ref = db.collection("mealPlan").document(user_email)
            ref.update(date, FieldValue.arrayUnion(data))
                .addOnSuccessListener {
                    Toast.makeText(this,"add success",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e -> Log.e("MSG", "Error updating document", e) }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                val bitmap = data!!.extras!!["data"] as Bitmap
                //recipe_user_image.setImageBitmap(bitmap)

                // save to firebase storage
                val storage: FirebaseStorage = FirebaseStorage.getInstance()
                // Create a storage reference from our app
                val storageRef = storage.getReferenceFromUrl("gs://final-project-cfa98.appspot.com")
                // Create a reference to "user_email_profile.jpg"
                val profileRef = storageRef.child(recipe_id.toString()+"_note.jpg")

                var baos: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                var data = baos.toByteArray()
                val uploadTask = profileRef.putBytes(data)
                uploadTask
                    .addOnFailureListener(OnFailureListener {
                        // Handle unsuccessful uploads
                    })
                    .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        loadNoteImage()
                    })
            }
        }
    }
    private fun parseMonth(month:Int):String {
        val list =  arrayListOf<String>("January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December")
        return list[month]
    }
}
