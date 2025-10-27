/**
 * Archivo: PreloaderScreen.kt
 *
 * Muestra una pantalla de carga inicial con el logo y un indicador de progreso.
 * Se usa al iniciar la app, antes de mostrar AuthScreen o GestorVentanas.
 */
/**
 * Archivo: PreloaderScreen.kt
 *
 * Muestra una pantalla de carga inicial con el logo y un indicador de progreso.
 * Incluye un fondo con degradado animado para mantener coherencia visual con AuthScreen.
 */
package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reto1_dam_2025_26.R

/**
 * Pantalla de pre-carga o "Splash" que se muestra brevemente al abrir la app.
 *
 * Usa un fondo degradado animado similar al login, el logo y un indicador de carga.
 */
@Composable
fun PreloaderScreen() {
    // Estado local para animar el degradado (invierte los colores cada 1.5s)
    var animateGradient by remember { mutableStateOf(false) }

    // Animación de colores del degradado (como en AuthScreen)
    val topColor by animateColorAsState(
        targetValue = if (animateGradient) Color(0xFFF09D90) else Color(0xFFB84332),
        animationSpec = tween(1500),
        label = "topColorAnim"
    )
    val bottomColor by animateColorAsState(
        targetValue = if (animateGradient) Color(0xFFB84332) else Color(0xFFF09D90),
        animationSpec = tween(1500),
        label = "bottomColorAnim"
    )

    // Inicia y repite la animación del degradado
    LaunchedEffect(Unit) {
        while (true) {
            animateGradient = !animateGradient
            kotlinx.coroutines.delay(1500)
        }
    }

    // Fondo degradado animado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(listOf(topColor, bottomColor))
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre de la app
            Text(
                text = "Mercado Ribera GO",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Indicador de carga
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f),
                strokeWidth = 4.dp
            )
        }
    }
}
