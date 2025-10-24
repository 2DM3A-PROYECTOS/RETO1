package com.example.reto1_dam_2025_26.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto1_dam_2025_26.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val address: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

class UserViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value, errorMessage = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onAddressChange(value: String) {
        _uiState.value = _uiState.value.copy(address = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun login() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Completa todos los campos.")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            repo.loginEmail(email, password) { success, message ->
                _uiState.value = if (success) {
                    _uiState.value.copy(isLoggedIn = true, isLoading = false)
                } else {
                    _uiState.value.copy(isLoading = false, errorMessage = message ?: "Error al iniciar sesión.")
                }
            }
        }
    }

    fun register() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()
        val username = _uiState.value.username.trim()
        val address = _uiState.value.address.trim()

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Completa todos los campos.")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            repo.registerEmail(email, password) { success, message ->
                if (success) {
                    repo.addUserManager(email, password, username, address) { userSuccess, userMsg ->
                        _uiState.value = if (userSuccess) {
                            _uiState.value.copy(isLoggedIn = true, isLoading = false)
                        } else {
                            _uiState.value.copy(isLoading = false, errorMessage = userMsg ?: "Error al guardar datos de usuario.")
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message ?: "Error al registrar usuario.")
                }
            }
        }
    }

    fun resetError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
