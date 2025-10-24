@file:OptIn(ExperimentalMaterial3Api::class)

/**
 * Pantallas y componentes para autenticación de usuario.
 *
 * Proporciona la pantalla principal de login/registro ([AuthScreen]) y
 * los componentes reutilizables para la entrada de datos y botones.
 *
 * Incluye animaciones, manejo de estado y validación básica visual.
 *
 * @file AuthScreen.kt
 */
package com.example.reto1_dam_2025_26.userInt.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reto1_dam_2025_26.viewmodels.UserViewModel

/**
 * Pantalla principal para autenticación.
 *
 * Permite alternar entre las vistas de login y registro con animaciones y
 * actualiza el estado del usuario a través del [UserViewModel].
 *
 * Muestra mensajes Toast en caso de error o éxito en la autenticación.
 *
 * @param vm Instancia del [UserViewModel] para manejar el estado y eventos.
 * @param onLoggedIn Callback que se ejecuta tras un inicio de sesión o registro exitoso.
 */
@Composable
fun AuthScreen(
    vm: UserViewModel = viewModel(),
    onLoggedIn: () -> Unit = {}
) {
    var showLogin by remember { mutableStateOf(true) }
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    // Animacion de los colores en el fondo de la pantalla
    val topColor by animateColorAsState(
        targetValue = if (showLogin) Color(0xFFF09D90) else Color(0xFFB84332),
        animationSpec = tween(800), label = "topColorAnim"
    )
    val bottomColor by animateColorAsState(
        targetValue = if (showLogin) Color(0xFFB84332) else Color(0xFFF09D90),
        animationSpec = tween(800), label = "bottomColorAnim"
    )

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.resetError()
        }
    }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            val message = if (showLogin) {
                "Inicio de sesion exitoso!"
            } else {
                // Cuando el registro es exitoso, enviamos el correo
                com.example.reto1_dam_2025_26.utils.enviarCorreoRegistro(state.email)
                "Registro exitoso! Se ha enviado un correo de confirmación."
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            onLoggedIn()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topColor, bottomColor))),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = showLogin,
            transitionSpec = {
                val animTime = 700
                if (targetState) {
                    slideInVertically(
                        animationSpec = tween(animTime),
                        initialOffsetY = { fullHeight: Int -> -fullHeight }
                    ) + fadeIn() togetherWith
                            slideOutVertically(
                                animationSpec = tween(animTime),
                                targetOffsetY = { fullHeight: Int -> fullHeight }
                            ) + fadeOut()
                } else {
                    slideInVertically(
                        animationSpec = tween(animTime),
                        initialOffsetY = { fullHeight: Int -> fullHeight }
                    ) + fadeIn() togetherWith
                            slideOutVertically(
                                animationSpec = tween(animTime),
                                targetOffsetY = { fullHeight: Int -> -fullHeight }
                            ) + fadeOut()
                }

            },
            label = "auth_transition"
        ) { isLogin ->
            if (isLogin) {
                LoginCard(
                    email = state.email,
                    password = state.password,
                    onEmailChange = vm::onEmailChange,
                    onPasswordChange = vm::onPasswordChange,
                    onLoginClick = vm::login,
                    onRegisterClick = { showLogin = false },
                    isLoading = state.isLoading
                )
            } else {
                RegisterCard(
                    username = state.username,
                    email = state.email,
                    address = state.address,
                    password = state.password,
                    onUsernameChange = vm::onUsernameChange,
                    onEmailChange = vm::onEmailChange,
                    onAddressChange = vm::onAddressChange,
                    onPasswordChange = vm::onPasswordChange,
                    onRegisterClick = vm::register,
                    onBackClick = { showLogin = true },
                    isLoading = state.isLoading
                )

            }
        }
    }
}

/**
 * Tarjeta para iniciar sesión.
 *
 * Muestra campos para email y contraseña, botón para login y enlace para registrar cuenta.
 *
 * @param email Texto actual del campo email.
 * @param password Texto actual del campo contraseña.
 * @param onEmailChange Callback para actualizar el email.
 * @param onPasswordChange Callback para actualizar la contraseña.
 * @param onLoginClick Callback para el evento de login.
 * @param onRegisterClick Callback para mostrar la pantalla de registro.
 * @param isLoading Indica si la operación está en progreso para deshabilitar controles.
 */
@Composable
fun LoginCard(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean
) {
    AuthCard(
        title = "Bienvenid@s 👋",
        fields = {
            AuthTextField("Email", email, onEmailChange)
            Spacer(Modifier.height(16.dp))
            PasswordField(password, onPasswordChange)
        },
        buttonText = "Entrar",
        onButtonClick = onLoginClick,
        isLoading = isLoading,
        bottomText = "Crear la cuenta",
        onBottomClick = onRegisterClick
    )
}

/**
 * Tarjeta para registrar un nuevo usuario.
 *
 * Muestra campos para nombre de usuario, email, dirección y contraseña,
 * botón para registrar y enlace para volver al login.
 *
 * @param username Texto actual del campo nombre de usuario.
 * @param email Texto actual del campo email.
 * @param address Texto actual del campo dirección.
 * @param password Texto actual del campo contraseña.
 * @param onUsernameChange Callback para actualizar el nombre de usuario.
 * @param onEmailChange Callback para actualizar el email.
 * @param onAddressChange Callback para actualizar la dirección.
 * @param onPasswordChange Callback para actualizar la contraseña.
 * @param onRegisterClick Callback para el evento de registro.
 * @param onBackClick Callback para volver a la pantalla de login.
 * @param isLoading Indica si la operación está en progreso para deshabilitar controles.
 */
@Composable
fun RegisterCard(
    username: String,
    email: String,
    address: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean,
) {
    AuthCard(
        title = "Crear cuenta ✨",
        fields = {
            AuthTextField("Nombre de usuario", username, onUsernameChange)
            Spacer(Modifier.height(16.dp))
            AuthTextField("Email", email, onEmailChange)
            Spacer(Modifier.height(16.dp))
            AddressField("Address", address, onAddressChange)
            Spacer(Modifier.height(16.dp))
            PasswordField(password, onPasswordChange)
        },
        buttonText = "Registrarse",
        onButtonClick = onRegisterClick,
        isLoading = isLoading,
        bottomText = "Volver al login",
        onBottomClick = onBackClick
    )
}

/**
 * Componente reutilizable para las tarjetas de autenticación.
 *
 * Renderiza un título, campos de entrada, botón principal y texto inferior con acción.
 *
 * @param title Título que aparece en la tarjeta.
 * @param fields Composable que representa los campos de entrada personalizados.
 * @param buttonText Texto del botón principal.
 * @param onButtonClick Callback al pulsar el botón principal.
 * @param isLoading Indica si se debe mostrar un indicador de carga.
 * @param bottomText Texto para el botón inferior.
 * @param onBottomClick Callback al pulsar el botón inferior.
 */
@Composable
private fun AuthCard(
    title: String,
    fields: @Composable ColumnScope.() -> Unit,
    buttonText: String,
    onButtonClick: () -> Unit,
    isLoading: Boolean,
    bottomText: String,
    onBottomClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .widthIn(max = 400.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 12.dp,
        color = Color(0xFFF09D90)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            fields()

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onButtonClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                if (isLoading)
                    CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                else
                    Text(buttonText, color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onBottomClick) {
                Text(bottomText, color = Color.White)
            }
        }
    }
}

/**
 * Campo de texto con etiqueta para formularios de autenticación.
 *
 * @param label Etiqueta del campo.
 * @param value Valor actual del campo.
 * @param onValueChange Callback para actualizar el valor.
 */
@Composable
private fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = textFieldColors()
    )
}

/**
 * Campo de texto para la dirección.
 *
 * @param label Etiqueta del campo.
 * @param value Valor actual del campo.
 * @param onValueChange Callback para actualizar el valor.
 */
@Composable
private fun AddressField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = textFieldColors()
    )
}

/**
 * Campo de texto para la contraseña con opción de mostrar/ocultar texto.
 *
 * @param password Texto actual de la contraseña.
 * @param onPasswordChange Callback para actualizar la contraseña.
 */
@Composable
private fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña") },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                AnimatedContent(passwordVisible, label = "pw_icon_anim") { visible ->
                    Icon(
                        imageVector = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        colors = textFieldColors()
    )
}

/**
 * Colores personalizados para los campos de texto en modo oscuro y claro.
 *
 * @return Un [TextFieldColors] con la configuración de colores adecuada.
 */
@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedIndicatorColor = Color.White,
    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
    cursorColor = Color.White,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White.copy(alpha = 0.8f)
)