package com.wustlcse438sp20.myrecipe.data

import java.util.*


data class RecipeInformation (
    val vegetarian:Boolean,
    val vegan:Boolean,
    val glutenFree:Boolean,
    val dairyFree:Boolean,
    val veryHealthy:Boolean,
    val cheap:Boolean,
    val veryPopular:Boolean,
    val sustainable:Boolean,
    val weightWatcherSmartPoints:Int,
    val gaps:String,
    val lowFodmap:Boolean,
    val sourceUrl:String,
    val spoonacularSourceUrl:String,
    val aggregateLikes:Int,
    val spoonacularScore:Int,
    val healthScore:Int,
    val creditsText:String,
    val license:String,
    val sourceName:String,
    val pricePerServing:Float,
    val extendedIngredients:List<ExtendedIngredients>,
    val id:Int,
    val title: String,
    val readyInMinutes:Int,
    val servings:Int,
    //According to tests, image may be null
    val image:String?,
    val imageType:String,
    val summary:String,
    val cuisines:List<String>,
    val dishTypes:List<String>,
    val diets:List<String>,
    val occasions:List<String>,
    val winePairing:winePairing,
    val instructions:String,
    val analyzedInstruction:analyzedInstructions
)

data class recipesLoad(
    val recipes:List<RecipeInformation>
)

data class winePairing(
    val pairedWines:List<String>,
    val pairingText:String,
    val productMatches:List<productMatches>
)

data class productMatches(
    val id: Int,
    val title: String,
    val description:String,
    val price:String,
    val imageUrl:String,
    val averageRating:Double,
    val ratingCount:Int,
    val score:Float,
    val link:String
)

data class analyzedInstructions(
    val name:String,
    val steps:List<steps>
)

data class steps(
    val number:Int,
    val step:String,
    val ingredients:List<String>,
    val equipment:List<equipment>
)

data class equipment(
    val id: Int,
    val name: String,
    val image:String
)
