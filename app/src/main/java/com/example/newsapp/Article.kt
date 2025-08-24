package com.example.newsapp

data class Article(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class TopHeadlinesResponse(
    val articles: List<Article>
)