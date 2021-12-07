package dev.m13d.newsapp.model.api

import dev.m13d.newsapp.BuildConfig

object RetrofitClient {
    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = BuildConfig.API_KEY
}
