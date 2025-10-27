/**
 * Archivo: MainActivity.kt
 *
 * Actividad principal de la aplicación que configura el tema y la ventana.
 * Controla el flujo inicial de la app mostrando la pantalla de autenticación (AuthScreen)
 * o, si el usuario está autenticado, carga el gestor de ventanas principal (GestorVentanas).
 *
 * Implementa el modo Edge-to-Edge para aprovechar toda la pantalla.
 */
package com.example.reto1_dam_2025_26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.reto1_dam_2025_26.ui.theme.Reto1_DAM_202526Theme
import com.example.reto1_dam_2025_26.userInt.components.GestorVentanas
import com.example.reto1_dam_2025_26.userInt.screens.AuthScreen
import com.example.reto1_dam_2025_26.userInt.screens.PreloaderScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Reto1_DAM_202526Theme(dynamicColor = false) {
                Surface {
                    var loggedIn by remember { mutableStateOf(false) }
                    var showSplash by rememberSaveable { mutableStateOf(true) }

                    // Simula carga inicial
                    LaunchedEffect(Unit) {
                        delay(3200) // Cambia duración si quieres
                        showSplash = false
                    }

                    // Transición entre Splash y App
                    Crossfade(targetState = showSplash) { splash ->
                        if (splash) {
                            PreloaderScreen()
                        } else {
                            if (loggedIn) {
                                GestorVentanas()
                            } else {
                                AuthScreen(onLoggedIn = { loggedIn = true })
                            }
                        }
                    }
                }
            }
        }
    }
}
