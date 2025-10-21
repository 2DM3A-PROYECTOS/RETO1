@file:OptIn(ExperimentalMaterial3Api::class)

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

/* -------------------------------------------------------------------------- */
/*                              AUTH MAIN SCREEN                              */
/* -------------------------------------------------------------------------- */

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
                "Registro exitoso!"
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

/* -------------------------------------------------------------------------- */
/*                                 LOGIN CARD                                 */
/* -------------------------------------------------------------------------- */

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

/* -------------------------------------------------------------------------- */
/*                               REGISTER CARD                                */
/* -------------------------------------------------------------------------- */

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

/* -------------------------------------------------------------------------- */
/*                          REUSABLE COMPOSABLE BLOCKS                         */
/* -------------------------------------------------------------------------- */

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

/* -------------------------------------------------------------------------- */
/*                             TEXT FIELD COLORS                              */
/* -------------------------------------------------------------------------- */

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
