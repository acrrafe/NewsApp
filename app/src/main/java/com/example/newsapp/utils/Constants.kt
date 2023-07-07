package com.example.newsapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Constants {

    // COMPANION OBJECT ALLOWS US TO use the class without having to create an instance of it
    companion object {
        const val API_KEY = "5433a242b82b427794652a07c7bdd887"
        const val BASE_URL = "https://newsapi.org"

        fun DateFormat(oldstringDate: String?): String? {
            val newDate: String?
            val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(getCountry()))
            newDate = try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate)
                dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                oldstringDate
            }
            return newDate
        }

        fun getCountry(): String {
            val locale = Locale.getDefault()
            val country = java.lang.String.valueOf(locale.country)
            return country.lowercase(Locale.getDefault())
        }


    }
}