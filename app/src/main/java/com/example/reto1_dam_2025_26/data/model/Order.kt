package com.example.reto1_dam_2025_26.data.model

// Item que se guarda DENTRO del pedido

// Pedido con la lista de productos comprados
data class Order(
    val id: String = "",                 // se setea con el doc.id al crear
    val userId: String = "",             // UID del usuario que ordenó
    val items: List<OrderItem> = emptyList(), // productos comprados
    val total: Double = 0.0,             // suma de price * qty
    val status: String = "PENDING",      // PENDING | PAID | CANCELLED | SHIPPED
    val paymentMethod: String = "CARD",  // CARD | CASH | ...
    val shippingAddress: String = ""     // dirección simple (texto)
    // Sin Timestamp para evitar dependencias si no lo necesitas
)


data class OrderItem(
    val productId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val qty: Int = 1,
    val imageUrl: String = ""
)

