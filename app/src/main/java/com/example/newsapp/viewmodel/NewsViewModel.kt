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
import com.example.newsapp.models.SavedRequest
import com.example.newsapp.models.UserRequest
import com.example.newsapp.models.UserResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsApplication
import com.example.newsapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(val newsRepo: NewsRepository, application: Application) : AndroidViewModel(application) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = _userResponseLiveData

    val breakingNews : MutableLiveData<NetworkResult<UserResponse>> = MutableLiveData()

    val pageNumber = 1

    val categoryNews : MutableLiveData<NetworkResult<UserResponse>> = MutableLiveData()

    val getSavedNews = newsRepo.getAllSavedNews()

    init {
        getBreakingNews("ph")
    }


    fun getBreakingNews(code: String) = viewModelScope.launch {
        checkInternetandBreakingNews(code)
    }

    private suspend fun checkInternetandBreakingNews(code: String){
        breakingNews.postValue(NetworkResult.Loading())
        try{
            if (hasInternetConnection()){

                val response = newsRepo.getBreakingNews(code, pageNumber)
                breakingNews.postValue(handleNewsResponse(response))
            } else {

                breakingNews.postValue(NetworkResult.Error("NO INTERNET CONNECTION"))
            }

        } catch (t: Throwable){
            when (t){
                is IOException -> breakingNews.postValue(NetworkResult.Error("NETWORK FAILER"))
                else -> breakingNews.postValue(NetworkResult.Error("Conversion Error"))
            }
        }
    }

    private fun handleNewsResponse(response: Response<UserResponse>): NetworkResult<UserResponse>? {
        if (response.isSuccessful){
            response.body()?.let { resultresponse->
                return NetworkResult.Success(resultresponse)
            }
        }

        return NetworkResult.Error(response.message())
    }
    fun getCategory(cat: String) = viewModelScope.launch {

        categoryNews.postValue(NetworkResult.Loading())
        val response = newsRepo.getCategoryNews(cat)

        categoryNews.postValue(handleNewsResponse(response))
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
    fun insertArticle (savedRequest: SavedRequest) {
        insertNews(savedRequest)
    }
    fun insertNews(savedRequest: SavedRequest) = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.insertNews(savedRequest)
    }
    fun deleteAllArtciles() {
        deleteAll()
    }
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.deleteAll()
    }

}