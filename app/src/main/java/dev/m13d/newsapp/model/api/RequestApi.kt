package dev.m13d.newsapp.model.api

import dev.m13d.newsapp.model.entity.News
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestApi {

    @GET("top-headlines")
    fun getNewsList(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ) : Single<News>
    @GET("everything")
    fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = RetrofitClient.API_KEY
    ): Single<News>
}
