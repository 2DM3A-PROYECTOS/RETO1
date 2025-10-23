/**
 * ViewModel para gestionar la UI relacionada con productos.
 *
 * Proporciona el estado UI con la lista de productos agrupados por categoría y manejo de carga y errores.
 *
 * @file ProductsViewModel.kt
 */
package com.example.reto1_dam_2025_26.viewmodels

import androidx.lifecycle.ViewModel
import com.example.reto1_dam_2025_26.data.model.Product
import com.example.reto1_dam_2025_26.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Estado UI para la pantalla de productos.
 *
 * @property loading Indica si los datos están cargándose.
 * @property categories Mapa de categorías a la lista de productos correspondientes.
 * @property error Mensaje de error en caso de fallo, o null si no hay error.
 */
data class ProductsUiState(
    val loading: Boolean = true,
    val categories: Map<String, List<Product>> = emptyMap(),
    val error: String? = null
)

/**
 * ViewModel que maneja la lógica para cargar productos desde Firestore y exponerlos para la UI.
 *
 * @property repo Repositorio para acceder a los datos de Firestore.
 */
class ProductsViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState

    init {
        load()
    }

    /**
     * Carga todos los productos desde el repositorio y actualiza el estado UI.
     *
     * Agrupa los productos por categoría y maneja estados de carga y error.
     */
    fun load() {
        _uiState.value = _uiState.value.copy(loading = true, error = null)

        // Usa tu repo (ya hace callbacks en el hilo principal)
        repo.getAllProducts { list, err ->
            if (err != null) {
                _uiState.value = ProductsUiState(
                    loading = false,
                    categories = emptyMap(),
                    error = err
                )
            } else {
                val grouped = (list ?: emptyList())
                    .groupBy { it.category.ifBlank { "Otros" } }
                    .toSortedMap() // opcional: orden alfabético de categorías
                _uiState.value = ProductsUiState(
                    loading = false,
                    categories = grouped,
                    error = null
                )
            }
        }
    }
}