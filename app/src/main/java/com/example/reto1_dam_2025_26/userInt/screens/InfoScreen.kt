package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R

@Composable
fun InfoScreen(navController: NavController) {
    Surface() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mercado),
                        contentDescription = "Mercado",
                        modifier = Modifier
                            .size(300.dp)
                    )
                }
            }

            item {
                LazyRow (
                    verticalAlignment = Alignment.CenterVertically,
                    //modifier = Modifier.padding(16.dp)
                ){
                    item {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Teléfono",
                            tint = Color(0xFFB84332)
                            //horizontalAlignment = Alignment.CenterHorizontally,
                            //verticalArrangement = Arrangement.Center
                        )
                    }

                    item {
                        Text("   944 231 019")
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                LazyRow (
                    verticalAlignment = Alignment.CenterVertically,
                    //modifier = Modifier.padding(16.dp)
                ){
                    item {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color(0xFFB84332)
                        )
                    }

                    item {
                        Text("   info@zbk.bilbao.eus")
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                LazyRow (
                    verticalAlignment = Alignment.CenterVertically,
                    //modifier = Modifier.padding(16.dp)
                ){
                    item {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Dirección",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    item {
                        Text("   c/ de la Ribera s/n")
                    }
                }
            }
        }
    }
}
