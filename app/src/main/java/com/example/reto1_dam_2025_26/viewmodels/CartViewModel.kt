/**
 * ViewModel para gestionar la lógica del carrito de compras.
 *
 * Mantiene una lista observable de items en el carrito y proporciona operaciones para modificarla.
 *
 * @file CartViewModel.kt
 */
package com.example.reto1_dam_2025_26.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.reto1_dam_2025_26.data.model.Product

/**
 * Representa un ítem dentro del carrito de compras.
 *
 * @property product Producto agregado.
 * @property qty Cantidad del producto en el carrito.
 */
// Clase para los elementos del carrito
data class CartItem(
    val product: Product,
    val qty: Int = 1
)

/**
 * ViewModel que gestiona los elementos del carrito y sus cantidades.
 */
class CartViewModel : ViewModel() {

    private val _items = mutableStateListOf<CartItem>()
    /**
     * Lista inmutable de ítems actuales en el carrito.
     */
    val items: List<CartItem> get() = _items

    /**
     * Agrega un producto al carrito.
     *
     * Si el producto ya existe, incrementa su cantidad.
     *
     * @param product Producto a agregar.
     */
    fun add(product: Product) {
        val index = _items.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val current = _items[index]
            _items[index] = current.copy(qty = current.qty + 1)
        } else {
            _items.add(CartItem(product))
        }
    }

    /**
     * Incrementa la cantidad de un producto en el carrito.
     *
     * @param productId ID del producto cuya cantidad se incrementa.
     */
    fun increase(productId: String) {
        val index = _items.indexOfFirst { it.product.id == productId }
        if (index != -1) {
            val current = _items[index]
            _items[index] = current.copy(qty = current.qty + 1)
        }
    }

    /**
     * Disminuye la cantidad de un producto en el carrito.
     *
     * Si la cantidad llega a 0, elimina el producto del carrito.
     *
     * @param productId ID del producto cuya cantidad se disminuye.
     */
    fun decrease(productId: String) {
        val index = _items.indexOfFirst { it.product.id == productId }
        if (index != -1) {
            val current = _items[index]
            if (current.qty > 1) {
                _items[index] = current.copy(qty = current.qty - 1)
            } else {
                _items.removeAt(index)
            }
        }
    }

    /**
     * Limpia todos los ítems del carrito.
     */
    fun clear() = _items.clear()

    /**
     * Calcula el total acumulado del carrito (precio * cantidad de cada producto).
     *
     * @return Total en formato Double.
     */
    fun total(): Double = _items.sumOf { it.product.price * it.qty }
}