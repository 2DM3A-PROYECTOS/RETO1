/**
 * ViewModel para gestionar la autenticación de usuarios y su estado en la UI.
 *
 * Maneja el login, registro y actualización del estado UI relacionado.
 *
 * @file UserViewModel.kt
 */
package com.example.reto1_dam_2025_26.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto1_dam_2025_26.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado UI para autenticación de usuario.
 *
 * @property id ID único del usuario.
 * @property username Nombre de usuario.
 * @property email Correo electrónico del usuario.
 * @property address Dirección del usuario.
 * @property password Contraseña ingresada (solo para UI).
 * @property isLoading Indica si se está procesando una operación de autenticación.
 * @property errorMessage Mensaje de error si ocurre un fallo, o null si no hay error.
 * @property isLoggedIn Indica si el usuario está autenticado correctamente.
 */
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

/**
 * ViewModel que gestiona la lógica de autenticación y registro de usuarios.
 *
 * Utiliza [FirestoreRepository] para interactuar con Firebase Auth y Firestore.
 *
 * @property repo Repositorio para operaciones de Firestore y autenticación.
 */
class UserViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    /**
     * Estado observable de la UI para autenticación.
     */
    val uiState: StateFlow<AuthUiState> = _uiState

    /**
     * Actualiza el nombre de usuario en el estado UI.
     *
     * @param value Nuevo valor para el nombre de usuario.
     */
    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value, errorMessage = null)
    }

    /**
     * Actualiza el correo electrónico en el estado UI.
     *
     * @param value Nuevo valor para el correo electrónico.
     */
    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    /**
     * Actualiza la dirección en el estado UI.
     *
     * @param value Nuevo valor para la dirección.
     */
    fun onAddressChange(value: String) {
        _uiState.value = _uiState.value.copy(address = value, errorMessage = null)
    }

    /**
     * Actualiza la contraseña en el estado UI.
     *
     * @param value Nuevo valor para la contraseña.
     */
    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    /**
     * Intenta iniciar sesión con el correo y contraseña proporcionados.
     *
     * Valida los campos, actualiza el estado UI y maneja la respuesta del repositorio.
     */
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
                if (success) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        repo.getUserData(uid) { data, dataError ->
                            _uiState.value = if (data != null) {
                                _uiState.value.copy(
                                    id = data["id"] as? String ?: "",
                                    username = data["username"] as? String ?: "",
                                    address = data["address"] as? String ?: "",
                                    email = data["email"] as? String ?: "",
                                    isLoggedIn = true,
                                    isLoading = false
                                )
                            } else {
                                _uiState.value.copy(
                                    isLoading = false,
                                    errorMessage = dataError ?: "Error al cargar datos del usuario."
                                )
                            }
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "No se pudo obtener UID del usuario."
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = message ?: "Error al iniciar sesión."
                    )
                }
            }
        }
    }

    /**
     * Intenta registrar un nuevo usuario con los datos proporcionados.
     *
     * Valida los campos, actualiza el estado UI y maneja la respuesta del repositorio.
     */
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
            repo.registerEmail(email, password) { success, message, uid ->
                if (success && uid != null) {
                    repo.addUserManager(uid, email, username, address) { userSuccess, userMsg ->
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

    /**
     * Limpia cualquier mensaje de error presente en el estado UI.
     */
    fun resetError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}