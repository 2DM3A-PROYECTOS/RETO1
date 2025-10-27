package com.example.reto1_dam_2025_26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reto1_dam_2025_26.ui.theme.Reto1_DAM_202526Theme
import com.example.reto1_dam_2025_26.userInt.components.GestorVentanas
import com.example.reto1_dam_2025_26.userInt.screens.AuthScreen
import com.example.reto1_dam_2025_26.userInt.screens.PreloaderScreen
import com.example.reto1_dam_2025_26.viewmodels.UserViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Reto1_DAM_202526Theme(dynamicColor = false) {
                Surface {
                    // ViewModel de usuario en el scope de la actividad
                    val userViewModel: UserViewModel = viewModel()

                    // Splash
                    var showSplash by rememberSaveable { mutableStateOf(true) }

                    // Estado local de navegación (no tocamos AuthScreen)
                    var loggedIn by rememberSaveable { mutableStateOf(false) }

                    // Restaurar sesión si existe y traer perfil
                    LaunchedEffect(Unit) {
                        userViewModel.loadCurrentUserIfNeeded()
                        delay(1200)
                        showSplash = false
                    }

                    // Si ya viene sesión desde Firebase (post-splash), entra directo
                    val uiState by userViewModel.uiState.collectAsState()
                    LaunchedEffect(uiState.isLoggedIn, showSplash) {
                        if (!showSplash && uiState.isLoggedIn) {
                            loggedIn = true
                        }
                    }

                    Crossfade(targetState = showSplash) { splash ->
                        if (splash) {
                            PreloaderScreen()
                        } else {
                            if (loggedIn) {
                                // No pasamos parámetros para evitar errores de firma
                                GestorVentanas(
                                    onLogout = {
                                        userViewModel.clearSession()  // ⬅️ limpia isLoggedIn, email, address, etc.
                                        loggedIn = false              // vuelve al AuthScreen
                                    }
                                )


                            } else {
                                // Mantener tu AuthScreen SIN cambios:
                                // - usa su propio vm interno
                                // - nos avisa con onLoggedIn()
                                AuthScreen(
                                    onLoggedIn = { loggedIn = true }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
