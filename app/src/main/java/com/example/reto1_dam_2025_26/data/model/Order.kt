/**
 * Modelo de datos para representar una orden de compra en el sistema.
 *
 * Contiene la definición de las clases [Order] y [OrderItem], que representan una orden y sus productos asociados.
 *
 * @file Order.kt
 */
package com.example.reto1_dam_2025_26.data.model

/**
 * Representa una orden de compra realizada por un usuario.
 *
 * @property id ID único de la orden.
 * @property userId UID del usuario que realizó la orden.
 * @property items Lista de productos comprados en la orden.
 * @property total Suma total del precio de los productos multiplicados por la cantidad.
 * @property status Estado actual de la orden (PENDING | PAID | CANCELLED | SHIPPED).
 * @property paymentMethod Método de pago utilizado (CARD | CASH | ...)
 * @property shippingAddress Dirección de envío.
 */
data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val total: Double = 0.0,
    val status: String = "PENDING",
    val paymentMethod: String = "CARD",
    val shippingAddress: String = ""
)

/**
 * Representa un ítem o producto dentro de una orden de compra.
 *
 * @property productId ID del producto.
 * @property name Nombre del producto.
 * @property price Precio unitario del producto.
 * @property qty Cantidad de unidades compradas.
 * @property imageUrl URL de la imagen del producto.
 */
data class OrderItem(
    val productId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val qty: Int = 1,
    val imageUrl: String = ""
)