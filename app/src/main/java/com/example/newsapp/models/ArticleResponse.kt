package com.example.newsapp.models

/** MODEL OF ARTICLE RESPONSE
    This is responsible for being the structure of every news in the API
    that contains the list of Article that have been requested and it's status and results **/
data class ArticleResponse(
    val articles: List<ArticleRequest>,
    val status: String,
    val totalResults: Int
)