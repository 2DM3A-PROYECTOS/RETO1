package com.example.reto1_dam_2025_26.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.reto1_dam_2025_26.data.model.Product

// Esta clase NO se guarda en la base de datos
data class CartItem(
    val product: Product,
    var qty: Int = 1
)

class CartViewModel : ViewModel() {

    private val _items = mutableStateListOf<CartItem>()
    val items: List<CartItem> get() = _items

    fun add(product: Product) {
        val existing = _items.find { it.product.id == product.id }
        if (existing != null) existing.qty++ else _items.add(CartItem(product))
    }

    fun remove(productId: String) {
        _items.removeAll { it.product.id == productId }
    }

    fun increase(productId: String) {
        _items.find { it.product.id == productId }?.let { it.qty++ }
    }

    fun decrease(productId: String) {
        val item = _items.find { it.product.id == productId } ?: return
        if (item.qty <= 1) _items.remove(item) else item.qty--
    }

    fun clear() = _items.clear()

    fun total(): Double = _items.sumOf { it.product.price * it.qty }
}
