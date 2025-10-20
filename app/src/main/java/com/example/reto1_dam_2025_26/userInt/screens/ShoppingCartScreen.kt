package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.viewmodels.CartViewModel

@Composable
fun ShoppingCartScreen(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems = cartViewModel.items

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            // Encabezado de la cesta
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp, 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = "Cesta",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(
                        text = "Detalle del pedido:",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Lista dinámica de productos
            items(cartItems.size) { index ->
                val item = cartItems[index]

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.imagenprueba), // Imagen genérica por ahora
                        contentDescription = item.product.name,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = item.product.description ?: "",
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "${item.qty} uds.",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Precio e IVA
            item {
                val total = cartViewModel.total()
                val iva = total * 0.21
                val totalConIva = total + iva

                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Precio:",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = String.format("%.2f €", total),
                            fontSize = 16.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "IVA (21%):",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = String.format("%.2f €", iva),
                            fontSize = 16.sp
                        )
                    }

                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total con IVA:",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = String.format("%.2f €", totalConIva),
                            fontSize = 20.sp
                        )
                    }
                }
            }

            // Botón de comprar
            item {
                Button(
                    onClick = {
                        navController.navigate("compra") {
                            popUpTo("cesta") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(text = "Comprar")
                }
            }
        }
    }
}


/*package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R

@Composable
fun ShoppingCartScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            // Header del carrito
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp, 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = "Cesta",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(
                        text = "Detalle del pedido:",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Repetir productos
            items(4) { index ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.imagenprueba),
                        contentDescription = "Producto $index",
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Filete de ternera",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = "paquete de 500 gramos",
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "2 uds.",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Línea divisoria
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Precio e iva
            item {
                Row(
                    modifier = Modifier
                        .padding(8.dp, 30.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio: ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = "48 €",
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Iva: ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = "21 %",
                        fontSize = 20.sp
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        navController.navigate("compra") {
                            popUpTo("cesta") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(text = "Comprar")
                }
            }
        }
    }
}*/