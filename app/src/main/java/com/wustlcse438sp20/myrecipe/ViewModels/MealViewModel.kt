package com.wustlcse438sp20.myrecipe.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wustlcse438sp20.myrecipe.Repositories.MealRepository
import com.wustlcse438sp20.myrecipe.Repositories.RecipeRepository
import com.wustlcse438sp20.myrecipe.data.*

class MealViewModel (application: Application):AndroidViewModel(application){
     val mealRepository = MealRepository()
     var mealList:MutableLiveData<MealSmart> = MutableLiveData()


    fun getMealplanner(targetCalories:Long){
        mealRepository.mealplanner(mealList,targetCalories)
    }
    fun saveEx(){
        mealRepository.saveExample()
    }
    fun queryMealByDay(){
        mealRepository.queryMealByDay()
    }

}