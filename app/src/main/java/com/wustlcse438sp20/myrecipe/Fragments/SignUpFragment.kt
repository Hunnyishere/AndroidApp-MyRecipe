package com.wustlcse438sp20.myrecipe.Fragments

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.data.UserProfile
import com.wustlcse438sp20.myrecipe.data.Collection
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignUpFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        auth = FirebaseAuth.getInstance()

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_signup.setOnClickListener(){
            if (signup_email.text!=null&&signup_email.text.toString()!="" && signup_password.text!=null && signup_password.text.toString()!=""){
                val email = signup_email.text.toString()
                val password = signup_password.text.toString()
                if(isEmail(email)){
                    button_signup.isEnabled = false
                    createAccount(email,password)
                }else{
                    Toast.makeText(context,"Please Inpute a valid email",Toast.LENGTH_SHORT).show()
                }
            }else
                Toast.makeText(context, "Please Input email or password.",
                    Toast.LENGTH_SHORT).show()
        }
    }

    fun isEmail(strEmail:String):Boolean {
        val strPattern =
            "^[a-zA-Z0-9][\\w\\.-]*@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"
        if (TextUtils.isEmpty(strPattern)) {
            return false
        } else{
            val p: Pattern = Pattern.compile(strPattern)
            val m: Matcher = p.matcher(strEmail)
            return m.matches()
        }
    }

    fun createAccount(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
                    builder?.setMessage("Sign Up Successfully")
                    builder?.setPositiveButton("Confirm",
                        DialogInterface.OnClickListener{ dialog, which->
                            button_signup.isEnabled = true
                        })
                    builder?.create()?.show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_LONG).show()
                    button_signup.isEnabled = true
                }
            }
    }

    fun updateUI(user: FirebaseUser?){
        //create a new user
        val user_profile = UserProfile(
            user!!.email!!,
            "",
            "https://spoonacular.com/recipeImages/Baked-Cheese-Manicotti-633508.jpg",
            0f,
            0f,
            ""
        )

        //val bmp: Bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)

        //store values for the database
        val userMap: MutableMap<String, Any> = HashMap()
        userMap["email"] = user_profile.email
        userMap["username"] = user_profile.username
        userMap["height"] = user_profile.height
        userMap["weight"] = user_profile.weight
        userMap["goal"] = user_profile.goal

        // Add a new document with a generated ID
        db.collection("userProfile")
            .add(userMap)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Toast.makeText(context,  "user profile created in the database!",Toast.LENGTH_LONG).show()
            })
            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(context, "Failed to create user in the database!", Toast.LENGTH_LONG)
            })
        db.collection("mealPlan").document(user_profile.email)
            .set(mapOf("time" to System.currentTimeMillis()))
            .addOnCompleteListener {

            }
    }

}
