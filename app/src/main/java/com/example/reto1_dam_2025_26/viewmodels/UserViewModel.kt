package com.example.reto1_dam_2025_26.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto1_dam_2025_26.data.model.User
import com.example.reto1_dam_2025_26.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val id: String = "",
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

    // -----------------------
    // Inputs formulario
    // -----------------------
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

    // -----------------------
    // Autenticación
    // -----------------------
    fun login() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Completa todos los campos.")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            repo.loginEmail(email, password) { success, msg ->
                if (success) {
                    // Tras login, carga el perfil completo (User en Firestore)
                    loadCurrentUser()
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = msg)
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
                    val uid = repo.currentUid() ?: return@registerEmail
                    // Guardar el documento del usuario (según tu repo actual)
                    repo.addUserManager(uid, email, username, address) { userSuccess, userMsg ->
                        if (userSuccess) {
                            // Reflejar inmediatamente los datos recién guardados
                            _uiState.value = _uiState.value.copy(
                                id = uid,
                                username = username,
                                email = email,
                                address = address,
                                isLoggedIn = true,
                                isLoading = false,
                                errorMessage = null
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = userMsg ?: "Error al guardar datos del usuario."
                            )
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = message ?: "Error al registrar usuario."
                    )
                }
            }
        }
    }

    // -----------------------
    // Carga de perfil
    // -----------------------
    fun loadCurrentUserIfNeeded() {
        val uid = repo.currentUid()
        val state = _uiState.value
        if (uid.isNullOrBlank()) return
        // Si ya tenemos datos, no recargamos
        if (state.id == uid && state.address.isNotBlank() && state.email.isNotBlank()) return
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val uid = repo.currentUid() ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            repo.getUserData(uid) { data, error ->
                if (data != null) {
                    val user = mapToUser(data)
                    _uiState.value = _uiState.value.copy(
                        id = user.id.ifBlank { uid },
                        username = user.username,
                        email = user.email,
                        address = user.address,
                        isLoggedIn = true,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        id = uid,
                        isLoggedIn = true,   // hay sesión aunque no haya doc (evita loops)
                        isLoading = false,
                        errorMessage = error ?: "No se pudo cargar el perfil."
                    )
                }
            }
        }
    }

    // -----------------------
    // Utilidades
    // -----------------------
    fun resetError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Mapea el documento (Map<String, Any>) a tu modelo `User`.
     * Evita ClassCastException y campos nulos/ausentes.
     */
    private fun mapToUser(map: Map<String, Any>): User {
        val id = (map["id"] as? String).orEmpty()
        val username = (map["username"] as? String).orEmpty()
        val email = (map["email"] as? String).orEmpty()
        val address = (map["address"] as? String).orEmpty()
        @Suppress("UNCHECKED_CAST")
        val orders = (map["orders"] as? List<String>) ?: emptyList()

        return User(
            id = id,
            username = username,
            address = address,
            email = email,
            orders = orders
        )
    }

    // Limpia toda la sesión local del ViewModel (sin tocar Firebase)
    fun clearSession() {
        _uiState.value = AuthUiState()
    }

}
