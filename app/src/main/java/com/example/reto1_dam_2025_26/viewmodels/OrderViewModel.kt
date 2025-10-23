/**
 * ViewModel para gestionar la creación de órdenes y su estado UI asociado.
 *
 * Provee métodos para crear una orden y manejar estados de carga, éxito y error.
 *
 * @file OrderViewModel.kt
 */
package com.example.reto1_dam_2025_26.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto1_dam_2025_26.data.model.OrderItem
import com.example.reto1_dam_2025_26.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado UI para la pantalla de creación de órdenes.
 *
 * @property isLoading Indica si se está procesando la creación de la orden.
 * @property errorMessage Mensaje de error si la creación falla, o null si no hay error.
 * @property successOrderId ID de la orden creada con éxito, o null si no se ha creado.
 */
data class OrderUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successOrderId: String? = null
)

/**
 * ViewModel que maneja la lógica para crear órdenes y exponer el estado a la UI.
 *
 * @property repo Repositorio para acceder a la base de datos Firestore.
 */
class OrderViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    /**
     * Estado observable de la UI para la creación de órdenes.
     */
    val uiState: StateFlow<OrderUiState> = _uiState

    /**
     * Crea una orden con los datos proporcionados.
     *
     * Valida que el userId y la lista de items no estén vacíos antes de enviar la orden.
     *
     * @param userId ID del usuario que realiza la orden.
     * @param items Lista de items a incluir en la orden.
     * @param paymentMethod Método de pago elegido.
     * @param shippingAddress Dirección de envío.
     */
    fun createOrder(
        userId: String,
        items: List<OrderItem>,
        paymentMethod: String,
        shippingAddress: String
    ) {
        if (userId.isEmpty()) {
            _uiState.value = OrderUiState(errorMessage = "User ID no puede estar vacío")
            return
        }
        if (items.isEmpty()) {
            _uiState.value = OrderUiState(errorMessage = "La lista de items está vacía")
            return
        }

        _uiState.value = OrderUiState(isLoading = true, errorMessage = null, successOrderId = null)

        viewModelScope.launch {
            repo.createOrder(userId, items, paymentMethod, shippingAddress) { success, error, orderId ->
                _uiState.value = if (success) {
                    OrderUiState(isLoading = false, successOrderId = orderId)
                } else {
                    OrderUiState(isLoading = false, errorMessage = error ?: "Error desconocido al crear la orden")
                }
            }
        }
    }
}