package com.example.reto1_dam_2025_26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.reto1_dam_2025_26.ui.theme.Reto1_DAM_202526Theme
import com.example.reto1_dam_2025_26.userInt.components.GestorVentanas

/**
 * Inicia la aplicación con la ventana de login
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Reto1_DAM_202526Theme(dynamicColor = false) {
                Surface {
                    //LoginScreen()
                    GestorVentanas()
                }
            }
        }
    }
}
