package com.wustlcse438sp20.myrecipe.Repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.wustlcse438sp20.myrecipe.NetworkTools.ApiClient
import com.wustlcse438sp20.myrecipe.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RecipeRepository {
    val service=
        ApiClient.makeRetrofitService()
//    val apiKey ="d062cbb99dd64369a1a4958a4cf1c751"
//    val apiKey ="28bb80cceac342b298452511c30c732f"
//    val apiKey ="3d97dfa37191404d8f9a8a2c2123820c"
//    val apiKey = "b524e6ef8e5c42228c4997b84c2d432d"
    val apiKey="7d1b1e6c1311463882baef4c5b9af1e6"
    fun searchRecipeByIngredients(resBody:MutableLiveData<List<RecipeByIngredients>>, ingredients: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchRecipeByIngredients(ingredients,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }

    fun searchRecipeByName(resBody:MutableLiveData<RecipeByName>, name: String, number:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchRecipeByName(name,number,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }


    fun searchRecipeByRandom(resBody:MutableLiveData<recipesLoad>){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchRecipeByRandom(12,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }

    fun searchRecipeInformation(resBody:MutableLiveData<RecipeInformation>,recipeId:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchRecipeInformation(recipeId,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }


    fun searchSimilarRecipes(resBody:MutableLiveData<List<SimilarRecipe>>,recipeId:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchSimilarRecipes(recipeId,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }
    fun getRecipeNutritionById(recipeId:Int,result: (RecipeNutrition)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getRecipeNutritionById(recipeId,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        response.body()?.let {
                            result(response.body()!!)
                        }
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }

}