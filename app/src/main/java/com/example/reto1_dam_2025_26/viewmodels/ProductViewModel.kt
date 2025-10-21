package com.example.reto1_dam_2025_26.viewmodels

import androidx.lifecycle.ViewModel
import com.example.reto1_dam_2025_26.data.model.Product
import com.example.reto1_dam_2025_26.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProductsUiState(
    val loading: Boolean = true,
    val categories: Map<String, List<Product>> = emptyMap(),
    val error: String? = null
)

class ProductsViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState

    init {
        load()
    }

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
