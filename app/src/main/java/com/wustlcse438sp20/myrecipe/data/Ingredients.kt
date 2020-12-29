package com.wustlcse438sp20.myrecipe.data

data class Ingredients (
    val id:Int,
    val amount:Double,
    val unit:String,
    val unitLong:String,
    val unitShort: String,
    val aisle:String,
    val name:String,
    val original:String,
    val originalString:String,
    val originalName: String,
    val metaInformation: List<String>,
    val meta:List<String>,
    val image:String
)