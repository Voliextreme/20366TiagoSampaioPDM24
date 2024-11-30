package com.example.thenewsapi.data.remote.model

import com.example.thenewsapi.domain.model.New

// Whole object structure of the API response
data class ApiResponse(
    val meta: Meta,
    val data: List<NewDto>
)

// Meta object structure of the API response
data class Meta(
    val found: Int,
    val returned: Int,
    val limit: Int,
    val page: Int
)

// News object structure of the API response
data class NewDto(
    val uuid: String,
    val title: String,
    val description: String,
    val snippet: String,
    val url: String,
    val image_url: String,
    val published_at: String,
    val source: String
) {
    // Convert the NewsDto object to a News object
    fun toNew() : New {
        return New(
            id = uuid,
            title = title,
            description = description
        )
    }
}