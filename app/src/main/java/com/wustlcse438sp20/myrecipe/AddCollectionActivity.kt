package com.wustlcse438sp20.myrecipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.wustlcse438sp20.myrecipe.data.Collection
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.activity_add_collection.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class AddCollectionActivity : AppCompatActivity() {

    private var user_email:String = ""
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = applicationContext as MyApplication
        user_email = globalVariable.getEmail()!!
        setContentView(R.layout.activity_add_collection)

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings

    }

    override fun onStart() {
        super.onStart()

        val intent = intent
        val bundle = intent.extras
//        user_email = bundle!!.getString("user_email")!!


        save_collection_button.setOnClickListener(){
            if (edit_collection_name.text.toString() !="" && edit_collection_name.text !=null && edit_description.text.toString()!=""&& edit_description.text!=null){
                //create a new user
                val collection = Collection(
                    "",
                    user_email,
                    edit_collection_name.text.toString(),
                    edit_description.text.toString(),
                    ArrayList<RecipeShownFormat>()
                )

                //store values for the database
                val recipes:ArrayList<MutableMap<String,Any>> = ArrayList()
                val collectionMap: MutableMap<String, Any> = HashMap()
                collectionMap["email"] = user_email
                collectionMap["name"] = collection.name
                collectionMap["description"] = collection.description
                collectionMap["recipes"] = recipes


                // Add a new collection to collections database with a generated documentID
                db.collection("collections")
                    .add(collectionMap)
                    .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                        // Put the String to pass back into an Intent and close this activity
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                        Toast.makeText(this,  "collection created in the database!",Toast.LENGTH_LONG).show()
                        //collection_id = documentReference.id
                    })
                    .addOnFailureListener(OnFailureListener { e ->
                        Toast.makeText(this, "Failed to create collection in the database!", Toast.LENGTH_LONG)
                    })

            }else{
                Toast.makeText(this,"Please Input the valid content",Toast.LENGTH_SHORT).show()
            }

        }

        cancel_collection_button.setOnClickListener(){
            // Put the String to pass back into an Intent and close this activity
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}