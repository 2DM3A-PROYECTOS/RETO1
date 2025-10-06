package com.example.reto1_dam_2025_26.interfaces

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R

@Composable
fun InicioInterface(navController: NavController) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.mercado),
                contentDescription = "Mercado"
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text("MERCADO DE LA RIBERA", color = MaterialTheme.colorScheme.secondary)
        }
    }
}