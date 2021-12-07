package dev.m13d.newsapp.views.utils

import dev.m13d.newsapp.model.entity.Article

sealed class AppState {
    data class Success(val newsData: List<Article>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
