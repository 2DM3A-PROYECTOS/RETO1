/**
 * Pantalla de agradecimiento tras la realización de un pedido.
 *
 * Contiene la definición de la función composable [Thanks] que muestra un mensaje
 * de confirmación y agradecimiento al usuario.
 *
 * @file Thanks.kt
 */
package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R

/**
 * Pantalla composable que muestra un mensaje de agradecimiento tras la confirmación del pedido.
 *
 * @param navController Controlador de navegación para manejar las transiciones de pantalla.
 */
@Composable
fun Thanks(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            item {
                Text("¡PEDIDO REALIZADO!")
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Image(
                    painter = painterResource(id = R.drawable.outline_check_24),
                    contentDescription = "Ok"
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text("Gracias por tu compra")
            }
        }
    }
}