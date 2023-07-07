@file:Suppress("DEPRECATION")

package com.example.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.SavedArticle
import com.example.newsapp.models.ArticleResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository, application: Application) : AndroidViewModel(application) {
    private val _breakingNewsResponseLiveData = MutableLiveData<NetworkResult<ArticleResponse>>()
    val breakingNewsResponseLiveData: LiveData<NetworkResult<ArticleResponse>> get() = _breakingNewsResponseLiveData
    private val _categoryNewsLiveData = MutableLiveData<NetworkResult<ArticleResponse>>()
    val categoryResponseLiveData: LiveData<NetworkResult<ArticleResponse>> get() = _categoryNewsLiveData
    private val pageNumber = 1
    val getSavedNews = newsRepository.getAllSavedNews()
    init {
        getBreakingNews("us")
        getCategory("business")
    }
    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNewsResponseLiveData.postValue(NetworkResult.Loading())
        val response = newsRepository.getBreakingNews(countryCode, pageNumber)
        _breakingNewsResponseLiveData.value = response
    }
     fun getCategory(category: String) = viewModelScope.launch {
        _categoryNewsLiveData.postValue(NetworkResult.Loading())
        val response = newsRepository.getCategoryNews(category)
        _categoryNewsLiveData.value = response
    }
    private fun insertArticle (savedArticle: SavedArticle) {
        insertNews(savedArticle)
    }
    private fun insertNews(savedArticle: SavedArticle) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.insertNews(savedArticle)
    }
    private fun deleteAllArticles() {
        deleteAll()
    }
    private fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.deleteAll()
    }

}