package com.wustlcse438sp20.myrecipe.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.*
import com.wustlcse438sp20.myrecipe.Adapter.CollectionAdapter
import com.wustlcse438sp20.myrecipe.R

import kotlinx.android.synthetic.main.fragment_profile.*
import com.wustlcse438sp20.myrecipe.data.Collection
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private var collectionList: ArrayList<Collection> = ArrayList()
    //private var tempCollectionList: ArrayList<Collection> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CollectionAdapter
    private var user_email = ""
    private lateinit var db: FirebaseFirestore
    private val SECOND_ACTIVITY_REQUEST_CODE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = getActivity()?.getApplicationContext() as MyApplication
        user_email = globalVariable.getEmail()!!

        val activity_intent = activity!!.intent
        //user_email = activity_intent!!.extras!!.getString("user_email")!!

        // set an instance of firebase
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)
    }

    fun updateDisplay(){
        // display profile and collections for this user
        var globalVariable = getActivity()?.getApplicationContext() as MyApplication
        user_email = globalVariable.getEmail()!!
        db.collection("userProfile")
            .whereEqualTo("email", user_email)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->

                if (task.isSuccessful) {
                    Log.v("Search in database", "Sucess")
                    println("找到用户profile")
                    for (document in task.result!!) {
                        Log.v("user email is",user_email)
                        profile_email.text = user_email
                        profile_username.text = document.get("username").toString()
                        profile_height.text  = document.get("height").toString()+" cm"
                        profile_weight.text  = document.get("weight").toString()+" kg"
                        profile_goal.text  = "Goal: " + document.get("goal").toString()
                        if (document.get("image") !== null) {
                            val image_url = document.get("image").toString()
                            // load firebase storage image
                            val storage = FirebaseStorage.getInstance()
                            val storageRef = storage.reference
                            storageRef.child(user_email+"_profile.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener {
                                // Data for "images/user_email_profile.jpg" is returned, use this as needed
                                var bitmap:Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size);
                                Log.v("成功加载 bitmap",bitmap.toString())
                                profile_user_image.setImageBitmap(bitmap)
                            }.addOnFailureListener {
                                // Handle any errors
                                Log.v("加载失败 bitmap","")
                            }
                        }
                        //profile_user_image.setImageResource(R.drawable.profile_image)
                    }
                } else {
                    Log.v("Search in database", "Fail")
                    println("failed to get user profile data")
                }
            })

        // display collections for this user
        db.collection("collections")
            .whereEqualTo("email", user_email)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    Log.v("Search in database", "Sucess")
                    println("找到用户的collection")
                    collectionList.clear()
                    for (document in task.result!!) {
                        Log.v("!!!document recipes",document.get("recipes").toString())
                        Log.v("document id",document.id)
                        collectionList.add(Collection(id=document.id, email = document.get("email").toString(), name = document.get("name").toString(), description = document.get("description").toString(), recipes = document.get("recipes") as ArrayList<RecipeShownFormat>))
                    }
                    //collectionList = tempCollectionList
                    Log.v("跑到collectionList",collectionList.toString())
                    adapter.notifyDataSetChanged()
                    //Log.v("adapter","更新啦啦啦")
                }
                else {
                    Log.v("Search in database", "Fail")
                    println("failed to get collection data")
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // collection RecyclerView Adapter
        recyclerView = profile_collection_recyclerview
        adapter = CollectionAdapter(context, collectionList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        button_logout.setOnClickListener({
            FirebaseAuth.getInstance().signOut()
            activity!!.onBackPressed()
        })
        adapter.setOnItemClick(object: CollectionAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                Log.v("Click on Collection",position.toString())
                val intent = Intent(context, DisplayCollectionActivity::class.java)
                var bundle = Bundle()
                bundle.putString("collectionId",collectionList[position].id)
                //bundle.putString("user_email",user_email)
                intent.putExtras(bundle)
                //activity?.startActivity(intent)
                startActivityForResult(intent,SECOND_ACTIVITY_REQUEST_CODE)
            }
        })


        edit_profile_button.setOnClickListener() {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivityForResult(intent,SECOND_ACTIVITY_REQUEST_CODE)  // call it from fragment itself, not from activity
        }

        add_collection_button.setOnClickListener(){
            val intent = Intent(context, AddCollectionActivity::class.java)
            startActivityForResult(intent,SECOND_ACTIVITY_REQUEST_CODE)
        }

        // display profile and collections
        updateDisplay()

    }

    // This method is called when the second activity finishes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                val myreturn = data?.extras?.getString("return")
                // update the ui
                    var globalVariable = getActivity()?.getApplicationContext() as MyApplication
                    user_email = globalVariable.getEmail()!!
                    // display profile and collections
                    updateDisplay()
            }
        }
    }
}

