package com.example.newsapp.models
// NEWS DATA
data class UserResponse(
    val articles: List<UserRequest>,
    val status: String,
    val totalResults: Int
)