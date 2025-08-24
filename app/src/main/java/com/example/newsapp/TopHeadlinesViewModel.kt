package com.example.newsapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TopHeadlinesViewModel: ViewModel() {

    data class TopHeadlinesState(
        val loading: Boolean = true,
        val articles: List<Article> = emptyList(),
        val error: String? = null
    )

    private val _topHeadlinesState = mutableStateOf(TopHeadlinesState())
    val topHeadlinesState = _topHeadlinesState

    init {
        fetchTopHeadlines()
    }

    fun fetchTopHeadlines() {
        viewModelScope.launch {
            try {
                val response = topHeadlinesService.getTopHeadlines("US")
                _topHeadlinesState.value = _topHeadlinesState.value.copy(
                    loading = false,
                    articles = response.articles,
                    error = null
                )
            } catch (e: Exception) {
                _topHeadlinesState.value = _topHeadlinesState.value.copy(
                    loading = false,
                    error = "Can't fetch headlines. Error ${e.toString()}"
                )
                Log.d("FETCH","${_topHeadlinesState.value.error}")
            }
        }
    }
}