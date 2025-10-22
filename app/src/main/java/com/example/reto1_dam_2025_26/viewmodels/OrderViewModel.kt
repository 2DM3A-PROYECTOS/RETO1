package com.example.reto1_dam_2025_26.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto1_dam_2025_26.data.model.OrderItem
import com.example.reto1_dam_2025_26.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OrderUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successOrderId: String? = null
)

class OrderViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState

    /**
     * Crea una orden con los datos recibidos.
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

    /**
     * Limpia el estado, útil para resetear la UI después de mostrar mensajes.
     */
    fun resetState() {
        _uiState.value = OrderUiState()
    }


}
