package com.example.newsapp.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferenceInstance private constructor(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MODE", Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean){
        sharedPreferences.edit().putBoolean(key, value).apply()
    }
    fun getBoolean(key: String, value: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, value)
    }

    fun saveString(key: String, value: String){
        sharedPreferences.edit().putString(key, value).apply()
    }
    fun getString(key: String, value: String): String {
        return sharedPreferences.getString(key, value).toString()
    }

    companion object {
        @Volatile
        private var instance: SharedPreferenceInstance? = null

        fun getInstance(context: Context): SharedPreferenceInstance {
            return instance ?: synchronized(this){
                instance ?: SharedPreferenceInstance(context.applicationContext)
            }
        }

    }
}