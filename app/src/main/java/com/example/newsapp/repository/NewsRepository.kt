package com.example.newsapp.repository


import androidx.lifecycle.LiveData
import com.example.newsapp.models.ArticleResponse
import com.example.newsapp.models.SavedArticle
import com.example.newsapp.room.NewsDao
import com.example.newsapp.service.RetrofitInstance
import com.example.newsapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class NewsRepository(private val newsDao: NewsDao) {

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

    suspend fun getBreakingNews(code: String, pageNumber: Int): NetworkResult<ArticleResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getBreakingNews(code, pageNumber)
                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val errorMessage = errorObj.getString("message") // Adjust the key according to your error response structure
                    NetworkResult.Error(errorMessage)
                } else {
                    NetworkResult.Error("Something went wrong")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Network request failed: ${e.message}")
            }
        }
    }

    suspend fun getCategoryNews(code: String): NetworkResult<ArticleResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getByCategory(code)
                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val errorMessage = errorObj.getString("message") // Adjust the key according to your error response structure
                    NetworkResult.Error(errorMessage)
                } else {
                    NetworkResult.Error("Something went wrong")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Network request failed: ${e.message}")
            }
        }
    }

}