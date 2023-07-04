package com.example.newsapp.models
// NEWS DATA
data class ArticleResponse(
    val articles: List<ArticleRequest>,
    val status: String,
    val totalResults: Int
)