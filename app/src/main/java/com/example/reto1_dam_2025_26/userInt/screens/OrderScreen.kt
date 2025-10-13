package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.reto1_dam_2025_26.R
import java.text.NumberFormat
import java.util.Locale

// -------------------------
// MODELOS DE DATOS (demo)
// -------------------------
data class CartItem(
    val id: String,
    val name: String,
    val subtitle: String,
    val quantity: Int,
    val unitPrice: Double,
    val imageRes: Int = R.drawable.imagenprueba
)

data class OrderSummary(
    val items: List<CartItem>,
    val deliveryAddress: String,
    val deliveryWindow: String,
    val paymentMethod: String, // p.ej. "Visa •••• 1234"
    val ivaRate: Double = 0.21, // 21%
    val shippingCost: Double = 0.0
) {
    val subtotal: Double get() = items.sumOf { it.unitPrice * it.quantity }
    val ivaAmount: Double get() = subtotal * ivaRate
    val total: Double get() = subtotal + ivaAmount + shippingCost
}

// -------------------------
// HELPERS
// -------------------------
private fun money(value: Double): String =
    NumberFormat.getCurrencyInstance(Locale("es", "ES")).format(value)

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
// ITEM DEL CARRITO
// -------------------------
@Composable
private fun OrderItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                item.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "x${item.quantity} • ${money(item.unitPrice)} / ud.",
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            money(item.unitPrice * item.quantity),
            fontWeight = FontWeight.SemiBold
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
    // Si lo conectas con ViewModel/estado real, pasa el OrderSummary por parámetro
    demoSummary: OrderSummary? = null
) {
    val summary = demoSummary ?: OrderSummary(
        items = listOf(
            CartItem("1", "Filete de ternera", "Paquete 500 g", 2, 8.50),
            CartItem("2", "Manzanas Fuji", "Bandeja 1 kg", 1, 3.20),
            CartItem("3", "Pan artesanal", "Barra 300 g", 2, 1.30)
        ),
        deliveryAddress = "C/ Ribera 12, 2ºA — Bilbao",
        deliveryWindow = "Hoy, 18:00 - 20:00",
        paymentMethod = "Visa •••• 1234"
    )

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
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Atrás")
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
                items(summary.items) { item ->
                    OrderItemRow(item)
                    Divider()
                }

                // Entrega
                item {
                    DeliveryCard(summary.deliveryAddress, summary.deliveryWindow)
                }

                // Pago
                item {
                    PaymentCard(summary.paymentMethod)
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
                            KeyValueRow("Subtotal", money(summary.subtotal))
                            KeyValueRow("IVA (${(summary.ivaRate * 100).toInt()}%)", money(summary.ivaAmount))
                            if (summary.shippingCost != 0.0) {
                                KeyValueRow("Envío", money(summary.shippingCost))
                            }
                            Divider(Modifier.padding(vertical = 8.dp))
                            KeyValueRow("Total", money(summary.total), emphasize = true)
                        }
                    }
                }

                // Botones
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { showConfirm = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Confirmar pedido")
                        }
                        OutlinedButton(
                            onClick = { navController.navigate("carrito") },
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
                        Text("Total a pagar: ${money(summary.total)}")
                        Spacer(Modifier.height(4.dp))
                        Text("Se aplicará el método de pago ${summary.paymentMethod}.")
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

// -------------------------
// PREVIEW (opcional)
// -------------------------
@Preview(showBackground = true)
@Composable
private fun OrderScreenPreview() {
    val nav = rememberNavController()
    OrderScreen(navController = nav)
}
