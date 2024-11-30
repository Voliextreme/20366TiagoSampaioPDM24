package com.example.thenewsapi.domain.repository

import com.example.thenewsapi.domain.model.NewDetail

interface NewDetailRepository {
    // Function to get news detail
    suspend fun getNewDetail(newId: String): NewDetail
}