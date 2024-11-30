package com.example.thenewsapi.data.repository

import NewApi
import android.util.Log
import com.example.thenewsapi.domain.model.New
import com.example.thenewsapi.domain.repository.NewRepository

class NewRepositoryImpl(private val api: NewApi) : NewRepository {
    override suspend fun getNews(): List<New> {
        return api.getNews().data.map {
            it.toNew()
        }
    }
}