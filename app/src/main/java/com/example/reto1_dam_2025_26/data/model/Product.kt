package com.example.reto1_dam_2025_26.data.model

data class Product(
    val id: String = "",           // ID único del producto (autogenerado por Firestore)
    val name: String = "",         // nombre del producto
    val description: String = "",  // descripción del producto
    val price: Double = 0.0,       // precio
    val stock: Int = 0,            // unidades disponibles
    val imageUrl: String = "",     // URL de imagen en Firebase Storage
    val category: String = "",     // categoría del producto (ej: frutas, panadería)
    val storeName: String = ""     // nombre del comercio/vendedor
)
