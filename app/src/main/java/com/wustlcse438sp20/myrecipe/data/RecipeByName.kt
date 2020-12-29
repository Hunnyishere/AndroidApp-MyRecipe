package com.wustlcse438sp20.myrecipe.data

data class RecipeByName (
    val results:List<results>,
    val baseUri:String,
    val offset:Int,
    val number:Int,
    val totalResults:Int,
    val processingTimeMs:Int,
    val expires:Long,
    val isStale:Boolean
    )

data class results(
    val id:Int,
    val title:String,
    val readyInMinutes:Int,
    val servings:Int,
    val sourceUrl:String,
    val openLicense:Int,
    val image:String
)