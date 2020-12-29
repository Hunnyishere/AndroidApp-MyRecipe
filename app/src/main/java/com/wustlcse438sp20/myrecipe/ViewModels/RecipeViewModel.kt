package com.wustlcse438sp20.myrecipe.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wustlcse438sp20.myrecipe.Repositories.RecipeRepository
import com.wustlcse438sp20.myrecipe.data.*

class RecipeViewModel (application: Application):AndroidViewModel(application){
    public val recipeRepository = RecipeRepository()
    public var recipeList:MutableLiveData<List<RecipeByIngredients>> = MutableLiveData()
    public var recipeByName:MutableLiveData<RecipeByName> = MutableLiveData()
    public var recipeRandom:MutableLiveData<recipesLoad> = MutableLiveData()
    public var recipeDetail:MutableLiveData<RecipeInformation> = MutableLiveData()
    public var similarRecipes:MutableLiveData<List<SimilarRecipe>> = MutableLiveData()
    public var recipeNutrition:MutableLiveData<List<SimilarRecipe>> = MutableLiveData()

    fun searchRecipeByIngredients(ingredients:String){
        recipeRepository.searchRecipeByIngredients(recipeList,ingredients)
    }

    fun searchRecipeByName(name:String){
        recipeRepository.searchRecipeByName(recipeByName,name,12)
    }

    fun searchRecipeByRandom(){
        Log.v("跑了请求","1")
        recipeRepository.searchRecipeByRandom(recipeRandom)
    }

    fun searchRecipeInformation(recipeId:Int){
        recipeRepository.searchRecipeInformation(recipeDetail,recipeId)
    }

    fun searchSimilarRecipes(recipeId:Int){
        recipeRepository.searchSimilarRecipes(similarRecipes,recipeId)
    }

    fun getRecipeNutritionById(recipeId:Int,result: (RecipeNutrition)->Unit){
        recipeRepository.getRecipeNutritionById(recipeId,result)
    }
}