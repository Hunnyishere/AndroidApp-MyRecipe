package com.wustlcse438sp20.myrecipe.NetworkTools

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiMealClient {
    const val BASE_URL = "https://api.spoonacular.com"

    fun build():Retrofit{
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}