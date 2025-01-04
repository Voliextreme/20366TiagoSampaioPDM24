package com.example.lojapdm.domain.model

data class Cart (
    val id: String = "",
    val ownerId: String = "",
    val userIds: List<String> = emptyList(),
    val carIds: List<String> = emptyList(),
)