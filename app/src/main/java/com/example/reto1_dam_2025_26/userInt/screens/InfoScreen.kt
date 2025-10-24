/**
 * Pantalla de información del mercado que muestra detalles de contacto y ubicación.
 *
 * Contiene la imagen principal del Mercado de la Ribera y filas con información
 * de teléfono, email y dirección.
 *
 * @file InfoScreen.kt
 */
package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.ui.theme.Blanco
import com.example.reto1_dam_2025_26.ui.theme.RojoMercado

/**
 * Composable que muestra la pantalla de información del mercado con datos de contacto.
 *
 * @param navController Controlador de navegación para la pantalla.
 */
@Composable
fun InfoScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.mercado),
                            contentDescription = "Mercado de la Ribera",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            item { InfoRow(icon = Icons.Filled.Call,  text = "944 231 019") }
            item { InfoRow(icon = Icons.Filled.Email, text = "info@zbk.bilbao.eus") }
            item { InfoRow(icon = Icons.Filled.Home,  text = "c/ de la Ribera s/n") }
        }
    }
}

/**
 * Composable que muestra una fila con un icono y un texto asociado para información.
 *
 * @param icon Ícono que representa la información (por ejemplo: teléfono, email, dirección).
 * @param text Texto que se muestra junto al ícono.
 */
@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    darkTheme: Boolean = isSystemInDarkTheme(),

    text: String

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(

            imageVector = icon,
            contentDescription = null,
            tint = if (darkTheme) Blanco else RojoMercado // COLOR DE LOS ICONOS
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}