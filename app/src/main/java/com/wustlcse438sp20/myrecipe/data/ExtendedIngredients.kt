package com.wustlcse438sp20.myrecipe.data

data class ExtendedIngredients (
    //According to test, id may be null
    val id:Int?,
    val aisle:String,
    val image:String,
    val consistency:String,
    val name:String,
    val original:String,
    val originalString:String,
    val originalName:String,
    val amount:Float,
    val unit:String,
    val meta:List<String>,
    val metaInformation:List<String>,
    val measures:measures
    )

data class measures(
    val us:us,
    val metric:us
)

data class us(
    val amount:Float,
    val unitShort:String,
    val unitLong:String
)
