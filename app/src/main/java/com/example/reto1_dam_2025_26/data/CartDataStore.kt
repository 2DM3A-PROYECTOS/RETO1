/**
 * Clase que gestiona la persistencia del carrito de compra mediante DataStore.
 *
 * Convierte la lista de elementos del carrito a formato JSON utilizando Gson
 * y la guarda de forma local en el dispositivo. Permite guardar, recuperar
 * y limpiar los datos del carrito de manera reactiva usando flujos (Flow).
 *
 * @file CartDataStore.kt
 * @see com.example.reto1_dam_2025_26.viewmodels.CartItem
 */
package com.example.reto1_dam_2025_26.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.reto1_dam_2025_26.viewmodels.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Crea una instancia única de DataStore asociada al contexto de la aplicación.
 *
 * Se utiliza para guardar las preferencias de la aplicación, en este caso
 * el contenido del carrito de compra.
 */
val Context.cartDataStore by preferencesDataStore(name = "cart_data")

/**
 * Clase encargada de interactuar con DataStore para almacenar y recuperar
 * los elementos del carrito de compra de manera persistente.
 *
 * @property context Contexto de la aplicación necesario para acceder a DataStore.
 */
class CartDataStore(private val context: Context) {

    /** Clave usada en DataStore para guardar el JSON del carrito. */
    private val CART_KEY = stringPreferencesKey("cart_items")
    /** Instancia de Gson utilizada para serializar y deserializar los objetos del carrito. */
    private val gson = Gson()

    /**
     * Guarda la lista completa de elementos del carrito en DataStore.
     *
     * La lista se convierte a formato JSON antes de ser almacenada.
     *
     * @param items Lista de [CartItem] a guardar de forma persistente.
     */
    suspend fun saveCart(items: List<CartItem>) {
        val json = gson.toJson(items)
        context.cartDataStore.edit { prefs ->
            prefs[CART_KEY] = json
        }
    }

    /**
     * Recupera los elementos del carrito almacenados en DataStore.
     *
     * Devuelve un flujo ([Flow]) que emite la lista de [CartItem] guardada.
     * Si no hay datos, devuelve una lista vacía.
     *
     * @return [Flow] que emite la lista actual de productos guardados.
     */
    fun getCart(): Flow<List<CartItem>> {
        return context.cartDataStore.data.map { prefs ->
            val json = prefs[CART_KEY] ?: "[]"
            val type = object : TypeToken<List<CartItem>>() {}.type
            gson.fromJson(json, type)
        }
    }

    /**
     * Elimina todos los datos del carrito almacenados en DataStore.
     *
     * Se utiliza al vaciar completamente la cesta de compra.
     */
    suspend fun clearCart() {
        context.cartDataStore.edit { prefs ->
            prefs.remove(CART_KEY)
        }
    }
}
