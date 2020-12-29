package com.wustlcse438sp20.myrecipe.NetworkTools

import com.wustlcse438sp20.myrecipe.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MealInterface {

    @GET("mealplanner/generate")
    suspend fun  mealplanner(@Query("timeFrame") timeFrame: String="day"
                             ,@Query("targetCalories") targetCalories: Int = 1200
                            ,@Query("diet") diet: String=""
                            ,@Query("exclude") exclude: String=""
                            ,@Query("apiKey") apiKey: String):Response<MealSmart>


}