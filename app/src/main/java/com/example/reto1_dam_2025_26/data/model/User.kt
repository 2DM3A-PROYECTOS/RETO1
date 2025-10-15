package com.example.reto1_dam_2025_26.data.model

data class User(
    val id: String = "",
    val username: String = "",
    val address: String = "",
    val email: String = "",
    val orders: List<String> = emptyList()
)