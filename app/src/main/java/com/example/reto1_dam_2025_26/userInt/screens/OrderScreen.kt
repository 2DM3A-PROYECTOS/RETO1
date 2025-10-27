/**
 * Pantalla de confirmación de pedido en la aplicación.
 *
 * Muestra el resumen del pedido con la lista de productos,
 * detalles de entrega y pago, y opciones para confirmar o editar el pedido.
 *
 * Contiene componentes auxiliares para mostrar secciones y formatos de moneda,
 * además de lógica para crear la lista de ítems de la orden a partir del carrito.
 *
 * @file OrderScreen.kt
 */
package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.viewmodels.CartViewModel
import com.example.reto1_dam_2025_26.viewmodels.UserViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.runtime.collectAsState
import com.example.reto1_dam_2025_26.data.model.OrderItem
import com.example.reto1_dam_2025_26.viewmodels.CartItem
import com.example.reto1_dam_2025_26.viewmodels.OrderViewModel

/**
 * Formatea un valor numérico como moneda en formato español (€).
 *
 * @param value Valor numérico a formatear.
 * @return Cadena con el valor formateado como moneda.
 */
private fun money(value: Double): String {
    val esES = Locale.forLanguageTag("es-ES")   // ✅ en vez de Locale("es","ES")
    return NumberFormat.getCurrencyInstance(esES).format(value)
}
/**
 * Composable que muestra un título de sección con estilo definido.
 *
 * @param text Texto que se mostrará como título.
 */
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
    )
}

/**
 * Composable que muestra una fila con una clave y un valor,
 * con opción a enfatizar el valor.
 *
 * @param label Texto de la clave o etiqueta.
 * @param value Texto del valor asociado.
 * @param emphasize Indica si se debe enfatizar el valor (negrita).
 */
@Composable
private fun KeyValueRow(label: String, value: String, emphasize: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            value,
            color = if (emphasize) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// -------------------------
// BLOQUE: ENTREGA
// -------------------------
/**
 * Composable que muestra información sobre la entrega,
 * incluyendo dirección y ventana horaria.
 *
 * @param address Dirección de entrega.
 * @param window Ventana horaria de entrega.
 */
@Composable
private fun DeliveryCard(address: String, window: String) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocalShipping,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                SectionTitle("Entrega")
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(address, color = MaterialTheme.colorScheme.onSurface)
                    Text(window, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------
// BLOQUE: PAGO
// -------------------------
/**
 * Composable que muestra información sobre el método de pago utilizado.
 *
 * @param method Descripción del método de pago (ejemplo: Visa **** 1234).
 */
@Composable
private fun PaymentCard(method: String) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CreditCard,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Column {
                SectionTitle("Pago")
                Spacer(Modifier.height(4.dp))
                Text(method, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

// -------------------------
// PANTALLA PRINCIPAL
// -------------------------
/**
 * Pantalla composable que muestra la confirmación de pedido.
 *
 * Permite visualizar los productos en el carrito, detalles de entrega y pago,
 * resumen del pago con subtotal, IVA y total, y botones para confirmar o editar el pedido.
 *
 * También gestiona el diálogo de confirmación de compra y la navegación.
 *
 * @param navController Controlador de navegación para gestionar pantallas.
 * @param cartViewModel ViewModel que gestiona el estado y operaciones del carrito.
 * @param userViewModel ViewModel que contiene el estado del usuario.
 * @param orderViewModel ViewModel que gestiona la creación de pedidos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    orderViewModel: OrderViewModel
) {
    val cartItems = cartViewModel.items
    val subtotal: Double = cartViewModel.total()
    val ivaAmount: Double = subtotal * 0.21
    val total: Double = subtotal + ivaAmount
    val userState = userViewModel.uiState.collectAsState().value

    var showConfirm by rememberSaveable { mutableStateOf(false) }

    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // Top bar simple
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    "Confirmación de pedido",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }

                // Items
                item { SectionTitle("Tu pedido") }

                items(cartItems.size) { index ->
                    val item = cartItems[index]

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
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
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Text(
                                    item.product.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    item.product.description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    "x${item.qty} • ${money(item.product.price)} / ud.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = money(item.product.price * item.qty),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Entrega
                item { DeliveryCard(userState.address, "Hoy, 18:00 - 20:00") }

                // Pago
                item { PaymentCard("Visa **** 1234") }

                // Totales
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            SectionTitle("Resumen de pago")
                            Spacer(Modifier.height(8.dp))
                            KeyValueRow("Subtotal", money(subtotal))
                            KeyValueRow("IVA (21%)", money(ivaAmount))
                            HorizontalDivider(
                                Modifier.padding(vertical = 10.dp),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            )
                            KeyValueRow("Total", money(total), emphasize = true)
                        }
                    }
                }

                // Botones
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { showConfirm = true }, // diseño + confirmación
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Confirmar pedido", style = MaterialTheme.typography.titleMedium)
                        }

                        Button(
                            onClick = {
                                navController.navigate("cesta") {
                                    popUpTo("compra") { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )

                        ) {
                            Text("Editar carrito")
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (showConfirm) {
            AlertDialog(
                onDismissRequest = { showConfirm = false },
                title = { Text("¿Confirmar compra?") },
                text = {
                    Column {
                        Text("Total a pagar: ${money(total)}", color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(4.dp))
                        Text("Se aplicará el método de pago: Visa.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showConfirm = false
                            // Aquí podrías disparar un ViewModel.placeOrder()
                            val orderItems = createOrderItemList(cartItems)
                            orderViewModel.createOrder(
                                userState.id,
                                orderItems,
                                "CARD",
                                userState.address
                            )
                            // crear lista de ordenes para el usuario en base de datos
                            cartViewModel.clear()
                            navController.navigate("gracias") {
                                popUpTo("compra") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirm = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

/**
 * Crea una lista de objetos [OrderItem] a partir de los elementos del carrito.
 *
 * @param cartItems Lista de [CartItem] actualmente en el carrito.
 * @return Lista de [OrderItem] con los datos necesarios para crear un pedido.
 */
fun createOrderItemList(cartItems: List<CartItem>): List<OrderItem> {
    return cartItems.map { cartItem ->
        OrderItem(
            productId = cartItem.product.id,
            name = cartItem.product.name,
            price = cartItem.product.price,
            qty = cartItem.qty,
            imageUrl = cartItem.product.imageUrl
        )
    }
}
