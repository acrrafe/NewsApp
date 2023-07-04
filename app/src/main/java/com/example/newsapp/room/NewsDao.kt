package com.example.newsapp.room


import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.example.newsapp.models.SavedRequest

interface NewsDao {

    @Insert()
    suspend fun saveNews(saveRequest: SavedRequest)

    @Query("SELECT * FROM NEWSARTICLE")
    fun getNewsByID() : LiveData<SavedRequest>

    @Query("SELECT * FROM NEWSARTICLE")
    fun getAllNews() : LiveData<List<SavedRequest>>

    @Query("DELETE FROM NEWSARTICLE")
    fun deleteAllNews()
}