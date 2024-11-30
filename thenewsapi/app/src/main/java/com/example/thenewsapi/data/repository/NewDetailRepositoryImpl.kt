package com.example.thenewsapi.data.repository

import NewApi
import com.example.thenewsapi.domain.model.NewDetail
import com.example.thenewsapi.domain.repository.NewDetailRepository

class NewDetailRepositoryImpl(private val api: NewApi) : NewDetailRepository {
    override suspend fun getNewDetail(newId: String): NewDetail {
        // Call the API and convert the response to NewsDetail
        return api.getNewDetail(newId).toNewDetail()
    }
}