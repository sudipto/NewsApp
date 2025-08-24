package com.example.newsapp

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val news_api_key = BuildConfig.API_KEY

private class ApiKeyInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("apiKey",apiKey)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}

val loggingInterceptor = Interceptor { chain ->
    val request = chain.request()
    Log.d("RetrofitURL", "Request URL: ${request.url}")
    chain.proceed(request)
}

private val client = OkHttpClient.Builder()
    .addInterceptor(ApiKeyInterceptor(news_api_key))
//    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://newsapi.org/v2/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String
    ): TopHeadlinesResponse
}

val topHeadlinesService: ApiService = retrofit.create(ApiService::class.java)
