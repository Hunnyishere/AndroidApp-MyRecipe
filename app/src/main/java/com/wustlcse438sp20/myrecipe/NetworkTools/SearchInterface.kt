package com.wustlcse438sp20.myrecipe.NetworkTools

import com.wustlcse438sp20.myrecipe.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchInterface {
    @GET("findByIngredients")
    suspend fun searchRecipeByIngredients(@Query("ingredients") ingredients:String,@Query("apiKey") apiKey:String):Response<List<RecipeByIngredients>>

    @GET("search")
    suspend fun searchRecipeByName(@Query("query") query:String, @Query("number") number:Int,@Query("apiKey") apiKey:String):Response<RecipeByName>

    @GET("random")
    suspend fun searchRecipeByRandom(@Query("number") number:Int,@Query("apiKey") apiKey:String):Response<recipesLoad>

    @GET("{recipeId}/information")
    suspend fun  searchRecipeInformation(@Path("recipeId") recipeId:Int,@Query("apiKey") apiKey: String):Response<RecipeInformation>

    @GET("{recipeId}/similar")
    suspend fun  searchSimilarRecipes(@Path("recipeId") recipeId:Int,@Query("apiKey") apiKey: String):Response<List<SimilarRecipe>>

    @GET("{recipeId}/nutritionWidget.json")
    suspend fun  getRecipeNutritionById(@Path("recipeId") recipeId:Int,@Query("apiKey") apiKey: String):Response<RecipeNutrition>

}