package com.example.thenewsapi.presentation.new_list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.thenewsapi.domain.model.NewDetail
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDetailScreen(
    viewModel: NewDetailViewModel,
    newId: String,
    onBackPressed: () -> Unit
) {
    val newsDetail by viewModel.newDetail.collectAsState()

    LaunchedEffect(newId) {
        viewModel.fetchIndividualNew(newId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News Details") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        newsDetail?.let { detail ->
            NewsDetailContent(
                newsDetail = detail,
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            )
        } ?: LoadingIndicator()
    }
}

@Composable
fun NewsDetailContent(
    newsDetail: NewDetail,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        AsyncImage(
            model = newsDetail.imageUrl,
            contentDescription = newsDetail.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = newsDetail.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        NewsMetadata(newsDetail)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = newsDetail.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NewsMetadata(newsDetail: NewDetail) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Source: ${newsDetail.source}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = newsDetail.date.substringBefore('T'),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}