package com.example.reto1_dam_2025_26.interfaces

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.ui.theme.Reto1_DAM_202526Theme
import com.example.reto1_dam_2025_26.windowManager

@Composable
fun InicioInterface(navController: NavController) {

    LazyColumn(
        //modifier = Modifier.fillMaxSize(),
        //modifier = Modifier.fillMaxHeight()
        //horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.mercado),
                contentDescription = "Mercado",
                modifier = Modifier
                    .size(500.dp)  // Cambia el tamaño a 500x500 dp
                    .padding(15.dp)
            )
        }
/*
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
*/
        item {
            Text("MERCADO DE LA RIBERA", color = MaterialTheme.colorScheme.secondary)
        }
    }
}