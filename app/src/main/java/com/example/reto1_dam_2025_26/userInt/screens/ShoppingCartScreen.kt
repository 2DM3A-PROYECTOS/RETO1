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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.viewmodels.CartViewModel
import java.text.NumberFormat
import java.util.Locale

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
    val spacing = 16.dp
    val fmt = NumberFormat.getCurrencyInstance(Locale("es", "ES"))

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (cartItems.isEmpty()) {
            // Estado vacío sencillo y elegante
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Cesta vacía",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Tu cesta está vacía",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Añade productos desde la pantalla de Productos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Surface
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = spacing),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            // Encabezado
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = spacing)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = "Cesta",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Detalle del pedido",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.height(8.dp))
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            }

            // Lista dinámica de productos (cada uno en Card)
            items(
                items = cartItems,
                key = { it.product.id }
            ) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
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

                        Spacer(modifier = Modifier.width(14.dp))

                        // Nombre + precio unidad + subtotal
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.product.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Unidad: ${fmt.format(item.product.price)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Subtotal: ${fmt.format(item.product.price * item.qty)}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Controles de cantidad (contenedores redondeados)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón de menos
                            IconButton(
                                onClick = { cartViewModel.decrease(item.product.id) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "Disminuir cantidad"
                                )
                            }

                            // Cantidad
                            Text(
                                text = item.qty.toString(),
                                fontSize = 16.sp,
                                modifier = Modifier.width(40.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            // Botón de más
                            IconButton(
                                onClick = { cartViewModel.increase(item.product.id) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Aumentar cantidad"
                                )
                            }
                        }
                    }
                }
            }

            // Precio e IVA (en un bloque destacado suave)
            item {
                val total = cartViewModel.total()
                val iva = total * 0.21
                val totalConIva = total + iva

                Column(
                    modifier = Modifier
                        .padding(horizontal = spacing)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Precio",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = fmt.format(total),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "IVA (21%)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = fmt.format(iva),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total con IVA",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = fmt.format(totalConIva),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            //  Botón de compra (acción primaria)
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
                        .padding(horizontal = spacing, vertical = 8.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Comprar",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Botón de vaciar cesta (tono menos dominante)
            item {
                Button(
                    onClick = { cartViewModel.clear() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing)
                        .padding(bottom = spacing),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Vaciar cesta",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
