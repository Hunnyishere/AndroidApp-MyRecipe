package com.wustlcse438sp20.myrecipe

import android.app.Application
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MyApplication : Application() {

    private var user_email: String? = null

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

    }


    fun getEmail(): String? {

        return user_email
    }

    fun setEmail(aEmail: String) {

        user_email = aEmail
    }

}
