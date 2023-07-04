package com.example.newsapp.service

import com.example.newsapp.models.ArticleResponse
import com.example.newsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(@Query("country") countrycode: String = "us", @Query("page") pageNumber: Int = 1,
                                @Query("apiKey") apiKey : String = API_KEY): Response<ArticleResponse>
    @GET("v2/everything")
    suspend fun getByCategory(@Query("q") category: String = "", @Query("apiKey")
        apiKey : String = API_KEY): Response<ArticleResponse>

}