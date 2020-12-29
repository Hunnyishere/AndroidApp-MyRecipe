package com.wustlcse438sp20.myrecipe.data

data class SimilarRecipe (
    val id:Int,
    val title:String,
    val image:String,
    val imageUrls:List<String>,
    val readyInMinutes:Int,
    val servings:Int
)