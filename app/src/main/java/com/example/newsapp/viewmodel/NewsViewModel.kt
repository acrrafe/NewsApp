@file:Suppress("DEPRECATION")

package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.SavedArticle
import com.example.newsapp.models.ArticleResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsApplication
import com.example.newsapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(val newsRepo: NewsRepository, application: Application) : AndroidViewModel(application) {

    val _articleResponseLiveData = MutableLiveData<NetworkResult<ArticleResponse>>()
    val articleResponseLiveData: LiveData<NetworkResult<ArticleResponse>> get() = _articleResponseLiveData
    val pageNumber = 1
    val getSavedNews = newsRepo.getAllSavedNews()

    init {
        getBreakingNews("us")
    }
    fun getBreakingNews(code: String) = viewModelScope.launch {
        checkInternetandBreakingNews(code)
    }
    private suspend fun checkInternetandBreakingNews(code: String){
        _articleResponseLiveData.postValue(NetworkResult.Loading())
        val response = newsRepo.getBreakingNews(code, pageNumber)
        try{
            if (hasInternetConnection()){
                _articleResponseLiveData.value = response
            } else {
                _articleResponseLiveData.value = response
            }

        } catch (t: Throwable){
            when (t){
                is IOException -> _articleResponseLiveData.postValue(NetworkResult.Error("NETWORK FAILED"))
                else -> _articleResponseLiveData.postValue(NetworkResult.Error("Conversion Error"))
            }
        }
    }

    fun getCategory(cat: String) = viewModelScope.launch {
        _articleResponseLiveData.postValue(NetworkResult.Loading())
        val response = newsRepo.getCategoryNews(cat)
        _articleResponseLiveData.value = response
    }
    // checking connectivity
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    // get category
    fun insertArticle (savedArticle: SavedArticle) {
        insertNews(savedArticle)
    }
    fun insertNews(savedArticle: SavedArticle) = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.insertNews(savedArticle)
    }
    fun deleteAllArtciles() {
        deleteAll()
    }
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.deleteAll()
    }

}