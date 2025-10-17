
package com.example.reto1_dam_2025_26.userInt.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.data.model.Product
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

// --- Formateo de precios ---
private val currencyLocale = Locale.forLanguageTag("es-ES")
private fun money(v: Double) = NumberFormat.getCurrencyInstance(currencyLocale).format(v)

/**
 * Popup de producto con diseño limpio:
 * - Card blanca (usa theme.surface)
 * - Texto oscuro (usa theme.onSurface)
 * - Botones adaptados al tema
 */
@Composable
fun ProductPopup(
    isVisible: Boolean,
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    onGoToCart: () -> Unit,
    placeholderRes: Int = R.drawable.outline_add_shopping_cart_24
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    AnimatedVisibility(
        visible = isVisible, //  Solo se muestra si isVisible = true
        enter = fadeIn(), //  Animación de aparición
        exit = fadeOut() //  Animación de desaparición
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.onBackground.copy(alpha = 0.5f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.94f)          // algo de margen lateral
                    .wrapContentHeight()           // altura según contenido
                    .heightIn(max = 600.dp)        // tope de altura (ajusta si quieres)
                    .clip(RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            )
            {
                Box(modifier = Modifier.fillMaxSize()) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Imagen del producto
                        ProductImage(
                            imageUrl = product.imageUrl,
                            placeholderRes = placeholderRes,
                            height = 180.dp  // antes 200dp
                        )


                        Spacer(Modifier.height(20.dp))

                        // Nombre del producto
                        Text(
                            text = product.name,
                            style = typography.titleLarge.copy(
                                color = colors.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        // Categoría (badge)
                        if (product.category.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            CategoryBadge(
                                text = product.category,
                                backgroundColor = colors.secondaryContainer,
                                textColor = colors.onSecondaryContainer
                            )
                        }

                        // Descripción
                        if (product.description.isNotBlank()) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = product.description,
                                style = typography.bodyMedium.copy(
                                    color = colors.onSurfaceVariant,
                                    lineHeight = 20.sp
                                )
                            )
                        }

                        // Precio principal
                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = money(product.price),
                            style = typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = colors.primary
                            )
                        )

                        // Detalles secundarios (stock, tienda)
                        Spacer(Modifier.height(8.dp))
                        if (product.stock > 0) {
                            Text(
                                text = "Stock disponible: ${product.stock}",
                                style = typography.labelLarge.copy(color = colors.onSurfaceVariant)
                            )
                        } else {
                            Text(
                                text = "Sin stock disponible",
                                style = typography.labelLarge.copy(color = colors.error)
                            )
                        }

                        if (product.storeName.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Tienda: ${product.storeName}",
                                style = typography.labelLarge.copy(color = colors.onSurfaceVariant)
                            )
                        }

                        Spacer(Modifier.height(80.dp)) // espacio para los botones inferiores
                    }

                    // Botones inferiores

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        ActionButtons(
                            onAddToCart = onAddToCart,
                            onBuyNow = onBuyNow,
                            onGoToCart = onGoToCart
                        )
                    }
                }
            }

            // Botón de cerrar (en esquina superior)

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 8.dp, y = 8.dp)
                    .size(36.dp)
                    .background(colors.primary.copy(alpha = 0.15f), RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = colors.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProductImage(
    imageUrl: String,
    placeholderRes: Int,
    height: Dp = 180.dp
) {
    val modifier = Modifier
        .height(height)
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))

    if (imageUrl.isNotBlank()) {
        AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = modifier)
    } else {
        Image(painter = painterResource(placeholderRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = modifier)
    }
}


@Composable
private fun CategoryBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ActionButtons(
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    onGoToCart: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryContainer)
            ) {
                Text(
                    text = "Añadir al carrito",
                    style = typography.labelLarge.copy(color = colors.onPrimaryContainer)
                )
            }

            Button(
                onClick = onBuyNow,
                modifier = Modifier
                    .weight(0.4f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.secondary)
            ) {
                Text(
                    text = "Comprar",
                    style = typography.labelLarge.copy(color = colors.onSecondary)
                )
            }
        }

        Button(
            onClick = onGoToCart,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryContainer)
        ) {
            Text(
                text = "Ir al carrito",
                style = typography.labelLarge.copy(color = colors.onPrimaryContainer)
            )
        }
    }
}