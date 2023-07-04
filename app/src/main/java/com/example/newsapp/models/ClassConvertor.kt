package com.example.newsapp.models

import androidx.room.TypeConverter

class ClassConvertor {

    // ROOM CAN ONLY ACCESS PRIMITIVE DATA TYPE
    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name!!
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }
}