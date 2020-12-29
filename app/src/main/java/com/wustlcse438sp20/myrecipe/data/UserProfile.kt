package com.wustlcse438sp20.myrecipe.data

data class UserProfile(
    val email: String,
    var username: String,
    var image: String,
    var height: Float,
    var weight: Float,
    var goal: String
)