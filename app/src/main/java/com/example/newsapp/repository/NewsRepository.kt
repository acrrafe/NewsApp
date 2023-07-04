package com.example.newsapp.repository


import androidx.lifecycle.LiveData
import com.example.newsapp.models.SavedArticle
import com.example.newsapp.room.NewsDao
import com.example.newsapp.service.RetrofitInstance

class NewsRepository(val newsDao: NewsDao) {

    suspend fun insertNews(savedArticle: SavedArticle) {
        newsDao.saveNews(savedArticle)
    }

    fun getAllSavedNews() : LiveData<List<SavedArticle>> {
        return newsDao.getAllNews()
    }


    fun getNewsByid() : LiveData<SavedArticle> {
        return newsDao.getNewsByID()
    }

    fun deleteAll(){
        newsDao.deleteAllNews()
    }

    suspend fun getBreakingNews(code: String, pageNumber: Int) = RetrofitInstance.api.getBreakingNews(code, pageNumber)

    suspend fun getCategoryNews(code: String) = RetrofitInstance.api.getByCategory(code)

}