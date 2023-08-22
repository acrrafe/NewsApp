package com.example.newsapp.room


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.SavedArticle
@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // If data is already existing, replace it
    suspend fun saveNews(saveRequest: SavedArticle)

    @Query("SELECT * FROM NEWSARTICLE")
    fun getNewsByID() : LiveData<SavedArticle>

    @Query("SELECT * FROM NEWSARTICLE")
    fun getAllNews() : LiveData<List<SavedArticle>>

    @Query("DELETE FROM NEWSARTICLE")
    fun deleteAllNews()

    @Delete
    suspend fun deleteArticle(saveRequest: SavedArticle)
}