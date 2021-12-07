package dev.m13d.newsapp.model.entity

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
