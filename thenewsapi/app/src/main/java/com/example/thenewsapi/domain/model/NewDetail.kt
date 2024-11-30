package com.example.thenewsapi.domain.model

data class NewDetail(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val imageUrl: String,
    val source: String,
)