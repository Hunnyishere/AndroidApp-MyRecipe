package com.wustlcse438sp20.myrecipe

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.wustlcse438sp20.myrecipe.Adapter.MealAdapter
import com.wustlcse438sp20.myrecipe.Adapter.PlanAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.MealViewModel
import com.wustlcse438sp20.myrecipe.data.Meal
import kotlinx.android.synthetic.main.activity_meal_analysis_activity.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*
import kotlin.collections.ArrayList

class MealAnalysisActivity : AppCompatActivity() {
    lateinit var mealViewModel: MealViewModel
    private lateinit var db : FirebaseFirestore
    private var date = ""
    private var calories = 0L
    private var carbs = 0L
    private var fat = 0L
    private var protein = 0L
    private var user_email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_analysis_activity)
        mealViewModel = ViewModelProviders.of(this).get(MealViewModel::class.java)
        db = FirebaseFirestore.getInstance()
        date = intent.getStringExtra("date")
        meal_time.text = date
        meal_time.setOnClickListener {
            mealViewModel.saveEx()
        }
        mealViewModel.queryMealByDay()
        var globalVariable = applicationContext as MyApplication
        user_email = globalVariable.getEmail()!!
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val list = arrayListOf<Meal>(
            Meal(1,"https://spoonacular.com/recipeImages/933310-312x231.jpg",1,1,"Manicotti"),
            Meal(1,"https://spoonacular.com/recipeImages/933310-312x231.jpg",1,1,"Manicotti")
        )

        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        val ref = db.collection("mealPlan").document(intent.getStringExtra("user_email"))
            ref.get()
                .addOnSuccessListener {document ->
                    if (document != null) {
                        Log.e("TAG", "DocumentSnapshot data: ${document.data}")
                        if (document.data!![date] != null) {
                            val map = document.data!![date] as ArrayList<Map<String, Any>>
                            Log.e("TAG", map.toString())
                            recyclerView.adapter = PlanAdapter(map)
                            map.forEach {
                                calories += it["calories"] as Long
                                carbs += it["carbs"] as Long
                                fat += it["fat"] as Long
                                protein += it["protein"] as Long
                            }
                        } else {
                            val builder: AlertDialog.Builder? = this.let { AlertDialog.Builder(it) }
                            builder?.setMessage("There are no plans yet")
                            builder?.setPositiveButton("Confirm",
                                DialogInterface.OnClickListener{ dialog, which->
                                    finish()
                                })
                            builder?.create()?.show()
                        }
                    } else {
                        Log.e("TAG", "No such document")
                    }
                }
                .addOnFailureListener { e -> Log.e("MSG", "Error updating document", e) }
                .addOnCompleteListener {
                    caloriesIntake.text = "Calories:$calories"
                    fatText.text = "Fat:$fat"
                    proteinintake.text = "Protein:$protein"
                }


        db.collection("userProfile")
            .whereEqualTo("email", user_email)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    Log.v("Search in database", "Sucess")
                    println("找到用户profile")
                    for (document in task.result!!) {
                        Log.v("user email is",user_email)
                      val weight =  document.get("weight").toString().toFloat().toLong()
                      val goal =  document.get("goal").toString()
                        var k = 1.0
                        var p = 1.0
                        when (goal) {
                            "Lose Weight" ->{
                                k =0.8
                                p = 1.0
                            }
                            "Shape Body" ->{
                                k =0.9
                                p = 1.0
                            }
                            "Gain Muscle" ->{
                                k =1.0
                                p = 1.1
                            }
                        }
                        val caloriesSu = 2000*k
                        val proteinSu = 0.8*weight*p
                        caloriesText.text = "Calories:$caloriesSu"
                        proteinSuggested.text = "protein:$proteinSu"
                        val ref = db.collection("mealPlan").document(user_email)
                        val data = mapOf<String,Any>(
                            "caloriesSuggest" to caloriesSu,
                            "proteinSuggest" to proteinSu
                        )
                        ref.update(data)
                            .addOnFailureListener { e -> Log.e("MSG", "Error updating document", e) }
                            .addOnSuccessListener {
                                Log.e("MSG", "update success")
                            }
                    }
                } else {
                    Log.v("Search in database", "Fail")
                    println("failed to get user profile data")
                }
            })
    }
    fun showDialog(weight:Long) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("select a model")
        var selets = arrayOf("lose weight","shape body","gain muscle")

        builder.setSingleChoiceItems(selets,0, DialogInterface.OnClickListener { dialog, which ->

        })
        builder.setPositiveButton("YES",
            DialogInterface.OnClickListener { dialog, which ->

            })

        val  dialog: AlertDialog= builder.create()
        dialog.show()
    }
}
