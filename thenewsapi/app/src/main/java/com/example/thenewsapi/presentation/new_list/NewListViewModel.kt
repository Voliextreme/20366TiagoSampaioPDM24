package com.example.thenewsapi.presentation.new_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thenewsapi.data.repository.NewRepositoryImpl
import com.example.thenewsapi.domain.model.New
import com.example.thenewsapi.domain.use_case.GetNewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsListViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val repository = NewRepositoryImpl(api)
    private val getNewsUseCase = GetNewUseCase(repository)

    // As a state flow, news and isLoading can be observed by the UI to update the UI when the data changes
    val news = MutableStateFlow<List<New>>(emptyList())
    val isLoading = MutableStateFlow(false)

    // Fetch news from the API
    fun fetchNews() {
        viewModelScope.launch {
            // Set isLoading to true before fetching the news
            isLoading.value = true
            try {
                // Fetch the news from the API
                news.value = getNewsUseCase()
            } catch (e: Exception) {
                // If an error occurs, set news to an empty list
                news.value = emptyList()
            } finally {
                // Set isLoading to false after fetching the news
                isLoading.value = false
            }
        }
    }
}