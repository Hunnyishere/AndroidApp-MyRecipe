package com.wustlcse438sp20.myrecipe.NetworkTools

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    const val BASE_URL = "https://api.spoonacular.com/recipes/"

    fun makeRetrofitService():SearchInterface{
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(SearchInterface::class.java)
    }
}