@file:Suppress("DEPRECATION")

package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
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
import java.io.IOException

class NewsViewModel(private val newsRepository: NewsRepository, application: Application) : AndroidViewModel(application) {
    private val _breakingNewsResponseLiveData = MutableLiveData<NetworkResult<ArticleResponse>>()
    val breakingNewsResponseLiveData: LiveData<NetworkResult<ArticleResponse>> get() = _breakingNewsResponseLiveData
    private val _categoryNewsLiveData = MutableLiveData<NetworkResult<ArticleResponse>>()
    val categoryResponseLiveData: LiveData<NetworkResult<ArticleResponse>> get() = _categoryNewsLiveData
    private val _countryWCategoryResponseLiveData = MutableLiveData<NetworkResult<ArticleResponse>>()
    val countryWCategoryResponseLiveData: LiveData<NetworkResult<ArticleResponse>> get() = _countryWCategoryResponseLiveData
    private val pageNumber = 1
    val getSavedNews = newsRepository.getAllSavedNews()


    private var sharedPref: SharedPreferences = application.getSharedPreferences("MODE", Context.MODE_PRIVATE)
    private var settingCountry: String = sharedPref.getString("country", "us").toString()
    init {
        getCountryWCategory(settingCountry, "business")
        getCategory("business")
    }
     fun getBreakingNews(countryCode: String) = viewModelScope.launch {
         safeBreakingNewsCall(countryCode)
    }

    fun getCountryWCategory(countryCode: String, categoryName: String) = viewModelScope.launch {
     safeCountryWCategoryCall(countryCode, categoryName)
    }
     fun getCategory(category: String) = viewModelScope.launch {
    safeCategoryCall(category)
    }
     fun insertArticle (savedArticle: SavedArticle) {
        insertNews(savedArticle)
    }
    private fun insertNews(savedArticle: SavedArticle) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.insertNews(savedArticle)
    }
     fun deleteArticle(savedArticle: SavedArticle) = viewModelScope.launch(Dispatchers.IO){
        newsRepository.deleteArticle(savedArticle)
    }
    private suspend fun safeBreakingNewsCall(countryCode: String){
        _breakingNewsResponseLiveData.postValue(NetworkResult.Loading())
        try{
            if(hasInternetConnect()){
                val response = newsRepository.getBreakingNews(countryCode, pageNumber)
                _breakingNewsResponseLiveData.value = response
            }else{
                _breakingNewsResponseLiveData.postValue(NetworkResult.Error("No network connection"))
            }

        }catch (t: Throwable){
            when(t){
                is IOException -> _breakingNewsResponseLiveData.postValue(NetworkResult.Error("Network Failure"))
                else -> _breakingNewsResponseLiveData.postValue(NetworkResult.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeCountryWCategoryCall(countryCode: String, categoryName: String){
        _countryWCategoryResponseLiveData.postValue(NetworkResult.Loading())
        try{
            if(hasInternetConnect()){
                val response = newsRepository.getCountryWCategory(countryCode, categoryName, pageNumber)
                _countryWCategoryResponseLiveData.value = response
            }else{
                _countryWCategoryResponseLiveData.postValue(NetworkResult.Error("No network connection"))
            }

        }catch (t: Throwable){
            when(t){
                is IOException -> _countryWCategoryResponseLiveData.postValue(NetworkResult.Error("Network Failure"))
                else -> _countryWCategoryResponseLiveData.postValue(NetworkResult.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeCategoryCall(category: String){
        _categoryNewsLiveData.postValue(NetworkResult.Loading())
        try{
            if(hasInternetConnect()){
                val response = newsRepository.getCategoryNews(category)
                _categoryNewsLiveData.value = response
            }else{
                _categoryNewsLiveData.postValue(NetworkResult.Error("No network connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> _categoryNewsLiveData.postValue(NetworkResult.Error("Network Failure"))
                else -> _categoryNewsLiveData.postValue(NetworkResult.Error("Conversion Error"))
            }
        }
    }


    fun hasInternetConnect(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activityNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}