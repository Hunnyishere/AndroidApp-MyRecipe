package com.wustlcse438sp20.myrecipe.data

data class RecipeByIngredients (
    val id:Int,
    val title:String,
    val image:String,
    val imageType:String,
    val usedIngredientCount:Int,
    val missedIngredientCount:Int,
    val missedIngredients: List<Ingredients>,
    val usedIngredients: List<Ingredients>,
    val unusedIngredients: List<Ingredients>,
    val likes:Int
)


data class RecipeShownFormat(
    val id: Int,
    val title: String,
    val image: String
)




