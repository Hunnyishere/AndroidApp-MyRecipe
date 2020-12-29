package com.wustlcse438sp20.myrecipe.data

data class MealSmart(
    val meals: List<Meal>,
    val nutrients: Nutrients
)
data class Nutrients(
    val calories: Double, // 1735.81
    val carbohydrates: Double, // 235.17
    val fat: Double, // 69.22
    val protein: Double // 55.43
)

data class Meal(
    val id: Int, // 655219
    val image: String, // Peanut-Butter-And-Chocolate-Oatmeal-655219.jpg
//    val imageUrls: List<String>,
    val readyInMinutes: Int, // 45
    val servings: Int, // 1
    val title: String // Peanut Butter And Chocolate Oatmeal
)