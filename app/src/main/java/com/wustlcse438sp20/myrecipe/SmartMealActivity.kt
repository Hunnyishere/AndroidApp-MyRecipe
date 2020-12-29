package com.wustlcse438sp20.myrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.wustlcse438sp20.myrecipe.Adapter.MealAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.MealViewModel
import com.wustlcse438sp20.myrecipe.data.Meal
import kotlinx.android.synthetic.main.activity_smart_meal.*
import java.util.*

class SmartMealActivity : AppCompatActivity() {
    lateinit var mealViewModel: MealViewModel
    private lateinit var db : FirebaseFirestore
    var user_email = ""
    private var calories = 0L
    private var carbs = 0L
    private var fat = 0L
    private var protein = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = applicationContext as MyApplication
        user_email = globalVariable.getEmail()!!
        setContentView(R.layout.activity_smart_meal)

        mealViewModel = ViewModelProviders.of(this).get(MealViewModel::class.java)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mealViewModel.mealList.observe(this, Observer {
            val list = arrayListOf<Meal>()
            list.addAll(it.meals)
            recyclerView.adapter = MealAdapter(list)
        })
        initData()
    }

    private fun initData() {
        val calenders = Calendar.getInstance()
        calenders.add(Calendar.DAY_OF_MONTH, -1)
        val date = parseMonth(calenders.get(Calendar.MONTH)) + " " +calenders.get(Calendar.DAY_OF_MONTH)+ ","+calenders.get(
            Calendar.YEAR)
        val ref = db.collection("mealPlan").document(user_email)
        ref.get()
            .addOnSuccessListener {document ->
                if (document != null) {
                    if (document.data!![date] != null) {
                        val map = document.data!![date] as ArrayList<Map<String, Any>>
                        map.forEach {
                            calories += it["calories"] as Long
//                            carbs += it["carbs"] as Long
//                            fat += it["fat"] as Long
                            protein += it["protein"] as Long
                        }
                        val caloriesSuggest = document.data!!["caloriesSuggest"] as Double
                        val proteinSuggest = document.data!!["proteinSuggest"] as Double


                        caloriesIntake.text = "Calories: ${caloriesSuggest - calories}"
//                        fatText.text = "Fat:$fat"
                        proteinintake.text = "Protein: ${proteinSuggest - protein}"
                        if (caloriesSuggest - calories > 0) {
                            mealViewModel.getMealplanner(500)
                        } else {
                            mealViewModel.getMealplanner(200)
                        }

                    } else {
                        val caloriesSuggest = 2000L
                        val proteinSuggest =  45L
                        caloriesIntake.text = "Calories: ${caloriesSuggest} Kcal"
                        proteinintake.text = "Protein: ${proteinSuggest} g"
                        mealViewModel.getMealplanner(caloriesSuggest)
                    }
                } else {
                    Log.e("TAG", "No such document")
                }
            }

    }
    private fun parseMonth(month:Int):String {
        val list =  arrayListOf<String>("January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December")
        return list[month]
    }
}
