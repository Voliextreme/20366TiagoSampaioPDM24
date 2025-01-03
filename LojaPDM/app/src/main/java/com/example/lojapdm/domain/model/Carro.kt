package com.example.lojapdm.domain.model

data class Carro(
    val id: String = "",
    val marca: String = "",
    val modelo: String = "",
    val preco: Double = 0.0,
    val descricao: String = "",
)