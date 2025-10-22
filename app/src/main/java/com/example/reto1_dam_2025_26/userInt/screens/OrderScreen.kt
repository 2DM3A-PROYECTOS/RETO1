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
import com.example.reto1_dam_2025_26.viewmodels.AuthUiState
import com.example.reto1_dam_2025_26.viewmodels.CartViewModel
import com.example.reto1_dam_2025_26.viewmodels.UserViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.runtime.collectAsState
import com.example.reto1_dam_2025_26.data.model.OrderItem
import com.example.reto1_dam_2025_26.viewmodels.CartItem
import com.example.reto1_dam_2025_26.viewmodels.OrderViewModel

// -------------------------
// HELPERS
// -------------------------
private fun money(value: Double): String {
    val esES = Locale.forLanguageTag("es-ES")   // ✅ en vez de Locale("es","ES")
    return NumberFormat.getCurrencyInstance(esES).format(value)
}
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun KeyValueRow(label: String, value: String, emphasize: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.secondary)
        Text(
            value,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// -------------------------
// BLOQUE: ENTREGA
// -------------------------
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
                Icon(imageVector = Icons.Outlined.Home, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(address)
                    Text(window, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------
// BLOQUE: PAGO
// -------------------------
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
                Text(method)
            }
        }
    }
}

// -------------------------
// PANTALLA PRINCIPAL
// -------------------------
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

    Surface(Modifier.fillMaxSize()) {
        Column {
            // Top bar simple
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás")
                }
                Text(
                    "Confirmación de pedido",
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
                item {
                    SectionTitle("Tu pedido")
                }
                items(cartItems.size) { index ->
                    val item = cartItems[index]

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
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

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {

                            Text(
                                item.product.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                item.product.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                "x${item.qty.toString()} • ${money(item.product.price)} / ud.",
                                fontSize = 12.sp
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = String.format("%.2f €", item.product.price * item.qty),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                // Entrega
                item {
                    DeliveryCard(userState.address, "Hoy, 18:00 - 20:00")
                }

                // Pago
                item {
                    PaymentCard("Visa **** 1234")
                }

                // Totales
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            SectionTitle("Resumen de pago")
                            Spacer(Modifier.height(8.dp))
                            KeyValueRow("Subtotal", money(subtotal))
                            KeyValueRow("IVA (21%)", money(ivaAmount))
                            HorizontalDivider(Modifier.padding(vertical = 8.dp))
                            KeyValueRow("Total", money(total), emphasize = true)
                        }
                    }
                }

                // Botones
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                val orderItems = createOrderItemList(cartItems)
                                //orderViewModel.createOrder("7h8CC1lK8AfL8uE5DBEHZkNmI833", orderItems, "CARD", "C/ Gran Via, 22")
                                orderViewModel.createOrder(
                                    userState.id,
                                 orderItems,
                                 "CARD",
                                    userState.address)
                                // crear lista de ordenes para el usuario en base de datos
                                cartViewModel.clear()
                                navController.navigate("gracias") {
                                    popUpTo("compra") { inclusive = true }
                                    launchSingleTop = true }},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Confirmar pedido")
                        }
                        OutlinedButton(
                            onClick = { navController.navigate("cesta") {
                                popUpTo("compra") { inclusive = true }
                                launchSingleTop = true
                            } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(50)
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
                        Text("Total a pagar: ${money(total)}")
                        Spacer(Modifier.height(4.dp))
                        Text("Se aplicará el método de pago: Visa.")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showConfirm = false
                            // Aquí podrías disparar un ViewModel.placeOrder()
                            navController.navigate("pedido_exitoso")
                        }
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirm = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

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