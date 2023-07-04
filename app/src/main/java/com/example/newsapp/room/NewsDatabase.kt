package com.example.newsapp.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.ClassConvertor
import com.example.newsapp.models.SavedArticle


@Database(entities = [SavedArticle::class], version = 1)
@TypeConverters(ClassConvertor::class)
abstract class NewsDatabase : RoomDatabase() {

    // INITIALIZED NEWSDAO AS AN ABSTRACT
    abstract  val newsDao: NewsDao

    // SINGLETON
    companion object{
        // THIS MAKES THE FIELD IMMEDIATELY VISIBILE TO OTHER THREAD
        @Volatile
        private var INSTANCE : NewsDatabase? = null
        fun getInstance (context: Context) : NewsDatabase{
            synchronized(this){
                var instance  = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java,
                        "news_database").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }


}