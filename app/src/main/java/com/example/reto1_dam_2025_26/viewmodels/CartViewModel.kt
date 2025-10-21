package com.example.reto1_dam_2025_26.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.reto1_dam_2025_26.data.model.Product

// Clase para los elementos del carrito
data class CartItem(
    val product: Product,
    val qty: Int = 1
)

class CartViewModel : ViewModel() {

    private val _items = mutableStateListOf<CartItem>()
    val items: List<CartItem> get() = _items

    fun add(product: Product) {
        val index = _items.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val current = _items[index]
            _items[index] = current.copy(qty = current.qty + 1)
        } else {
            _items.add(CartItem(product))
        }
    }

    fun remove(productId: String) {
        _items.removeAll { it.product.id == productId }
    }

    fun increase(productId: String) {
        val index = _items.indexOfFirst { it.product.id == productId }
        if (index != -1) {
            val current = _items[index]
            _items[index] = current.copy(qty = current.qty + 1)
        }
    }

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

    fun clear() = _items.clear()

    fun total(): Double = _items.sumOf { it.product.price * it.qty }
}
