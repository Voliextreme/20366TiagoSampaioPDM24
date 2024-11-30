package com.example.thenewsapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.newsapp.presentation.news_list.NewListScreen
import com.example.thenewsapi.presentation.new_list.NewDetailScreen
import com.example.thenewsapi.presentation.new_list.NewDetailViewModel
import com.example.thenewsapi.presentation.new_list.NewsListViewModel
import com.example.thenewsapi.ui.theme.ThenewsapiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThenewsapiTheme(){
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedNewsId by remember { mutableStateOf<String?>(null) }

    if (selectedNewsId == null) {
        val newsListViewModel = NewsListViewModel()
        NewListScreen(newsListViewModel) { newId ->
            selectedNewsId = newId
        }
    } else {
        val newsDetailViewModel = NewDetailViewModel()
        NewDetailScreen(newsDetailViewModel, selectedNewsId!!) {
            selectedNewsId = null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ThenewsapiTheme {
        MainScreen()
    }
}