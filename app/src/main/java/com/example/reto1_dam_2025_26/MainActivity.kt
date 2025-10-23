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
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.reto1_dam_2025_26.ui.theme.Reto1_DAM_202526Theme
import com.example.reto1_dam_2025_26.userInt.components.GestorVentanas
import com.example.reto1_dam_2025_26.userInt.screens.AuthScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Reto1_DAM_202526Theme(dynamicColor = false) {
                Surface {
                    //Es_Una_Prueba()

                    var loggedIn by remember { mutableStateOf(false) }

                    if (loggedIn) {
                        GestorVentanas()
                    } else {
                        AuthScreen(
                            onLoggedIn = { loggedIn = true }
                        )
                    }
                }
            }
        }


    }
}
