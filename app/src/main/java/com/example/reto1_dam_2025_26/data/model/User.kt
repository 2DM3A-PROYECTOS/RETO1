/**
 * Modelo de datos para representar un usuario dentro del sistema.
 *
 * Contiene la definición de la clase [User] con sus propiedades principales.
 *
 * @file User.kt
 */
package com.example.reto1_dam_2025_26.data.model

/**
 * Representa un usuario registrado en el sistema.
 *
 * @property id ID único del usuario.
 * @property username Nombre de usuario o alias.
 * @property address Dirección del usuario.
 * @property email Correo electrónico del usuario.
 * @property orders Lista de IDs de órdenes asociadas al usuario.
 */
data class User(
    val id: String = "",
    val username: String = "",
    val address: String = "",
    val email: String = "",
    val orders: List<String> = emptyList()
)