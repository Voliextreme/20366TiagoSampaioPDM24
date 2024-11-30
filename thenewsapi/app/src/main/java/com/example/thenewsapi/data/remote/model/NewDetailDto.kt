package com.example.thenewsapi.data.remote.model

import com.example.thenewsapi.domain.model.NewDetail

data class NewDetailDto(
    val uuid: String,
    val title: String,
    val description: String,
    val snippet: String,
    val url: String,
    val image_url: String,
    val published_at: String,
    val source: String
) {
    // Convert to domain model
    fun toNewDetail() : NewDetail {
        return NewDetail(
            id = uuid,
            title = title,
            description = description,
            date = published_at,
            imageUrl = image_url,
            source = source
        )
    }
}