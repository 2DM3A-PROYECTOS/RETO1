/**
 * ViewModel para gestionar la lógica del carrito de compras con persistencia en DataStore.
 *
 * Mantiene una lista observable de items y la guarda automáticamente en caché.
 *
 * @file CartViewModel.kt
 */
package com.example.reto1_dam_2025_26.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto1_dam_2025_26.data.CartDataStore
import com.example.reto1_dam_2025_26.data.model.Product
import kotlinx.coroutines.launch

/**
 * Representa un ítem dentro del carrito de compras.
 */
data class CartItem(
    val product: Product,
    val qty: Int = 1
)

/**
 * ViewModel que gestiona los elementos del carrito y sus cantidades.
 * Usa DataStore para guardar los datos de forma persistente.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = CartDataStore(application)
    private val _items = mutableStateListOf<CartItem>()
    val items: List<CartItem> get() = _items

    init {
        // Al iniciar, cargamos el carrito guardado en DataStore
        viewModelScope.launch {
            dataStore.getCart().collect { savedItems ->
                _items.clear()
                _items.addAll(savedItems)
            }
        }
    }

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
        saveToCache()
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
            saveToCache()
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
            saveToCache()
        }
    }

    /**
     * Limpia todos los ítems del carrito.
     */
    fun clear() {
        _items.clear()
        viewModelScope.launch { dataStore.clearCart() }
    }

    /**
     * Calcula el total acumulado del carrito (precio * cantidad de cada producto).
     *
     * @return Total en formato Double.
     */
    fun total(): Double = _items.sumOf { it.product.price * it.qty }

    /**
     * Guarda el estado actual del carrito en DataStore.
     *
     * Convierte la lista de productos [_items] a formato JSON y la almacena
     * de forma persistente mediante la clase [CartDataStore].
     * Esta operación se ejecuta dentro de una corrutina del [viewModelScope],
     * garantizando que se realice en segundo plano sin bloquear la interfaz.
     *
     * Se invoca automáticamente cada vez que se añade, elimina o modifica
     * la cantidad de un producto en el carrito.
     *
     * @see com.example.reto1_dam_2025_26.data.CartDataStore.saveCart
     */
    private fun saveToCache() {
        viewModelScope.launch {
            dataStore.saveCart(_items)
        }
    }
}