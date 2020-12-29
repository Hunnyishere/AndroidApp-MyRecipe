package com.wustlcse438sp20.myrecipe.data

data class Collection (
    val id:String,
    val email:String,
    val name:String,
    val description:String,
    val recipes:ArrayList<RecipeShownFormat>
)