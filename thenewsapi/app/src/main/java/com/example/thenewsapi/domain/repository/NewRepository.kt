package com.example.thenewsapi.domain.repository

import com.example.thenewsapi.domain.model.New

interface NewRepository {
    suspend fun getNews():List<New>
}