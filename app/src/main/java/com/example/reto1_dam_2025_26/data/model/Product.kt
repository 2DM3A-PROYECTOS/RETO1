/**
 * Modelo de datos para representar un producto dentro del sistema.
 *
 * Contiene la definición de la clase [Product] con sus propiedades principales.
 *
 * @file Product.kt
 */
package com.example.reto1_dam_2025_26.data.model

/**
 * Representa un producto en el sistema.
 *
 * @property id ID único del producto.
 * @property name Nombre del producto.
 * @property description Descripción del producto.
 * @property price Precio del producto.
 * @property stock Cantidad de unidades disponibles en inventario.
 * @property imageUrl URL de la imagen del producto almacenada en Firebase Storage.
 * @property category Categoría a la que pertenece el producto (ejemplo: carnicería, pescadería...).
 * @property storeName Nombre del comercio o vendedor que ofrece el producto.
 */
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val imageUrl: String = "",
    val category: String = "",
    val storeName: String = ""
)