package com.wustlcse438sp20.myrecipe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.Adapter.SimpleItemTouchHelperCallback
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.Collection
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.activity_display_collection.*


class DisplayCollectionActivity : AppCompatActivity() {

    private var recipeList: ArrayList<RecipeShownFormat> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecipeAdapter
    var user_email:String = ""
    var collection_id:String = ""
    private lateinit var db : FirebaseFirestore
    lateinit var recipeviewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = applicationContext as MyApplication
        user_email = globalVariable.getEmail()!!
        setContentView(R.layout.activity_display_collection)
        val bundle = intent.extras
        collection_id = bundle!!.getString("collectionId")!!
        //user_email = bundle!!.getString("user_email")!!

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
        showCollection()
        delete_collection_button.setOnClickListener(){
            db.collection("collections").document(collection_id)
                .delete()
                .addOnCompleteListener(OnCompleteListener<Void> { task ->
                    if (task.isSuccessful) {
                        Log.v("Delete from database", "Sucess")
                        println("Delete collection success !!!!!!!!!!!!!!!!!!")
                        // Put the String to pass back into an Intent and close this activity
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    else {
                        Log.v("Delete from database", "Fail")
                        println("failed to delete collection")
                    }
                })

        }
        display_collection_return_button.setOnClickListener(){
            // Put the String to pass back into an Intent and close this activity
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }


    override fun onStart() {
        super.onStart()
    }

    fun showCollection(){

        val intent = intent
        val bundle = intent.extras
        collection_id = bundle!!.getString("collectionId")!!

        recipeList.clear()
        db.collection("collections")
            .whereEqualTo(FieldPath.documentId(), collection_id)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    Log.v("Search in database", "Sucess")
                    println("search success !!!!!!!!!!!!!!!!!!")
                    for (document in task.result!!) {
                        display_collection_name.text = document.get("name").toString()
                        for(recipe in document.get("recipes") as ArrayList<MutableMap<String,Any>>){
                            var recipe_id:Int =recipe["id"].toString().toInt()
                            var recipe_title:String = recipe["title"].toString()
                            var recipe_image:String = recipe["image"].toString()
                            Log.v("recipe id",recipe_id.toString())
                            Log.v("recipe title",recipe_title)
                            Log.v("recipe image",recipe_image)
                            recipeList.add(RecipeShownFormat(recipe_id,recipe_title,recipe_image))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                else {
                    Log.v("Search in database", "Fail")
                    println("failed to get user data")
                }
            })
        //RecyclerView Adapter
        recyclerView = recipe_in_collection_recyclerview
        adapter = RecipeAdapter(this,recipeList,collection_id)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = adapter
        adapter.setOnItemClick(object :RecipeAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                val detail_intent = Intent(this@DisplayCollectionActivity, RecipeInformationActivity::class.java)
                var bundle = Bundle()
                bundle.putInt("recipeId", recipeList[position].id)
                bundle.putString("type","collection")
                Log.v("send recipeId to detail",recipeList[position].id.toString())
                detail_intent.putExtras(bundle)
                startActivity(detail_intent)
            }
        })

        //callback ItemTouch
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter,collection_id)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
    }


}
