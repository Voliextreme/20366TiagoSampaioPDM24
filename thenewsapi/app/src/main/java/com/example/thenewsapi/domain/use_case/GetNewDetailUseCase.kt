package com.example.thenewsapi.domain.use_case

import com.example.thenewsapi.domain.model.NewDetail
import com.example.thenewsapi.domain.repository.NewDetailRepository

class GetNewDetailUseCase(private val repository: NewDetailRepository) {
    suspend operator fun invoke(newId:String): NewDetail{
        return repository.getNewDetail(newId)
    }
}