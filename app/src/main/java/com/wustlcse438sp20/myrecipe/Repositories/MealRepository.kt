package com.wustlcse438sp20.myrecipe.Repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.wustlcse438sp20.myrecipe.NetworkTools.ApiClient
import com.wustlcse438sp20.myrecipe.NetworkTools.ApiMealClient
import com.wustlcse438sp20.myrecipe.NetworkTools.MealInterface
import com.wustlcse438sp20.myrecipe.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.security.Timestamp
import java.util.*

class MealRepository {
    val service=
        ApiMealClient.build().create(MealInterface::class.java)
//    val apiKey ="d062cbb99dd64369a1a4958a4cf1c751"
//    val apiKey ="28bb80cceac342b298452511c30c732f"
//    val apiKey ="3d97dfa37191404d8f9a8a2c2123820c"
    val apiKey = "b524e6ef8e5c42228c4997b84c2d432d"
    val db = FirebaseFirestore.getInstance()
    fun mealplanner(resBody:MutableLiveData<MealSmart>,targetCalories:Long){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.mealplanner(apiKey=apiKey,targetCalories = targetCalories.toInt())
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){

                        val data = response.body()
                        Log.v("aaaa",data.toString())
                        resBody.value=data
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }

    fun saveExample() {
        val item:MutableMap<String,Any> = hashMapOf(
            "image" to "Peanut-Butter-And-Chocolate-Oatmeal-655219.jpg",
            "name" to "food",
            "title" to "Peanut Butter And Chocolate Oatmeal"
        )
        val docData:MutableMap<String,Any> = hashMapOf(
            "date" to "Mar 20,2013",
            "list" to arrayListOf(item, item, item)
        )

        db.collection("look@163.com").document("meal")
            .set(docData)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
    }
    fun queryMealByDay() {
        db.collection("look@163.com")
            .whereEqualTo("date", "Mar 20,2013")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

    }


}