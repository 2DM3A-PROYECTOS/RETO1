package com.example.reto1_dam_2025_26.interfaces

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun CatalogoInterface(navController: NavController) {
    LazyColumn {
        item {
            Text("prueba de visualización de ventana Catalogo")
        }
    }
}