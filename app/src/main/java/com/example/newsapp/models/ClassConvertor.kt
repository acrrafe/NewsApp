package com.example.newsapp.models

/** CLASS CONVERTOR
    This is responsible for converting the object of the data class to become accessible to other
    class **/
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