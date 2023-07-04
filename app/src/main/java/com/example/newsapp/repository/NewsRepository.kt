package com.example.newsapp.repository


import androidx.lifecycle.LiveData
import com.example.newsapp.models.SavedRequest
import com.example.newsapp.room.NewsDao
import com.example.newsapp.service.RetrofitInstance

class NewsRepository(val newsDao: NewsDao) {

    suspend fun insertNews(savedRequest: SavedRequest) {
        newsDao.saveNews(savedRequest)
    }

    fun getAllSavedNews() : LiveData<List<SavedRequest>> {
        return newsDao.getAllNews()
    }


    fun getNewsByid() : LiveData<SavedRequest> {
        return newsDao.getNewsByID()
    }

    fun deleteAll(){
        newsDao.deleteAllNews()
    }

    suspend fun getBreakingNews(code: String, pageNumber: Int) = RetrofitInstance.api.getBreakingNews(code, pageNumber)

    suspend fun getCategoryNews(code: String) = RetrofitInstance.api.getByCategory(code)

}