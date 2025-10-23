/**
 * Pantalla que muestra el contenido del carrito de compra del usuario,
 * permitiendo visualizar los productos añadidos, modificar sus cantidades,
 * mostrar el precio total con IVA incluido, y opciones para comprar o vaciar la cesta.
 *
 * Contiene una lista dinámica de productos con controles para aumentar o disminuir
 * la cantidad, muestra el precio, IVA y total, y ofrece botones para proceder a la compra
 * o vaciar el carrito.
 *
 * @file ShoppingCartScreen.kt
 */
package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.viewmodels.CartViewModel

/**
 * Composable que representa la pantalla del carrito de compra.
 *
 * Muestra la lista de productos en el carrito junto con controles para ajustar
 * las cantidades. También muestra el total, el IVA calculado y el total con IVA.
 * Ofrece botones para proceder a la compra o vaciar el carrito.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param cartViewModel ViewModel que maneja el estado y operaciones del carrito.
 * @return Composable que muestra la UI de la cesta de compra.
 */
@Composable
fun ShoppingCartScreen(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems = cartViewModel.items

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            // 🛒 Encabezado
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

            // 🧾 Lista dinámica de productos
            items(cartItems.size) { index ->
                val item = cartItems[index]

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen del producto
                    AsyncImage(
                        model = item.product.imageUrl,
                        contentDescription = item.product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(25.dp))

                    // Detalles y botones de cantidad
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = item.product.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        // Controles de cantidad
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Botón de menos
                            IconButton(
                                onClick = { cartViewModel.decrease(item.product.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "Disminuir cantidad",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            // Cantidad
                            Text(
                                text = item.qty.toString(),
                                fontSize = 16.sp,
                                modifier = Modifier.width(32.dp),
                                textAlign = TextAlign.Center
                            )

                            // Botón de más
                            IconButton(
                                onClick = { cartViewModel.increase(item.product.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Aumentar cantidad",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // 💶 Precio e IVA
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

            // 🛍️ Botón de compra
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

            item {
                Spacer(modifier = Modifier.width(25.dp))
            }

            // 🛍️ Botón de vaciar cesta
            item {
                Button(
                    onClick = {
                        cartViewModel.clear()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Vaciar cesta")
                }
            }
        }
    }
}