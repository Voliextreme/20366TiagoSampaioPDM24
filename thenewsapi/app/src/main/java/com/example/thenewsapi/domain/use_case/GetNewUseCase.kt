package com.example.thenewsapi.domain.use_case

import com.example.thenewsapi.domain.model.New
import com.example.thenewsapi.domain.repository.NewRepository

class GetNewUseCase(private val repository: NewRepository) {
    suspend operator fun invoke():List<New>{
        return repository.getNews()
    }
}