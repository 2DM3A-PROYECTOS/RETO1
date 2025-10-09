package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R

@Composable
fun ProductsScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,)
    {
        item{
            Text(
                text = "Carne",
                color = Color.Black,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp),
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Filete de lomo"
                        )
                        Text(
                            text = "Filete de lomo",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "10$", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp),
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Chuletillas de cordero"
                        )
                        Text(
                            text = "Chuletillas",
                            color = Color.Black
                        )
                        Text(text = "de cordero", color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "8.0$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Metro",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(text = "Metro", color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "7.5$", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Pechuga",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Pechuga ",
                            color = Color.Black
                        )
                        Text(
                            text = "de pollo",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "6$", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Chuleton",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Chuleton",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "64.5$",
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Pescado",
                color = Color.Black,
                fontSize = 40.sp
            )
            LazyRow {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Merluza",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Merluza",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "11.5$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Bacalao",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Bacalao",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "10.5$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Bonito",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Bonito",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "12$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter =
                                painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Salmon",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Salmon",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "15.8$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter =
                                painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Sardinas",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(
                                    1.5.dp,
                                    MaterialTheme.colorScheme.primary
                                )
                                .size(100.dp)
                        )
                        Text(
                            text = "Sardinas",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "11.6$",
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bebidas",
                fontSize = 40.sp,
                color = Color.Black
            )
            LazyRow {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter =
                                painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Zumo de naranja",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Zumo de naranja",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "5.6$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter =
                                painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Colo Cola",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Coca Cola",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "2.2$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter =
                                painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Red bool",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Red Bool",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "3.1$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter =
                                painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Agua",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Agua",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1.5$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Pepsi",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Pepsi",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "2.1$",
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Pasteles",
                color = Color.Black,
                fontSize = 40.sp
            )
            LazyRow{
                item{
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Pastel Vasco",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Pastel Vasco",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "24$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Tarta Pepisandwich",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Tarta",
                            color = Color.Black
                        )
                        Text(
                            text = "Pepisandwich",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "31.5$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Topper Feliz Cumple",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Topper",
                            color = Color.Black
                        )
                        Text(
                            text = "Feliz Cumple",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "8$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Caja Pepiboms",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Caja Pepiboms",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "9.6$",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = "Tarta Explosión de chocolate",
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                                .size(100.dp)
                        )
                        Text(
                            text = "Tarta Explosión",
                            color = Color.Black
                        )
                        Text(
                            text = "de chocolate",
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "5.9$",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}