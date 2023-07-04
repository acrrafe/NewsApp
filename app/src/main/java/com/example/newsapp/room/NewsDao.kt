package com.example.newsapp.room


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newsapp.models.SavedArticle
@Dao
interface NewsDao {

    @Insert()
    suspend fun saveNews(saveRequest: SavedArticle)

    @Query("SELECT * FROM NEWSARTICLE")
    fun getNewsByID() : LiveData<SavedArticle>

    @Query("SELECT * FROM NEWSARTICLE")
    fun getAllNews() : LiveData<List<SavedArticle>>

    @Query("DELETE FROM NEWSARTICLE")
    fun deleteAllNews()
}