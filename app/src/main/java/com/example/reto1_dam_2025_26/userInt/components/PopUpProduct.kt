
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.data.model.Product
import java.text.NumberFormat
import java.util.Locale

private val currencyLocale = Locale.forLanguageTag("es-ES")
private fun money(v: Double) = NumberFormat.getCurrencyInstance(currencyLocale).format(v)

@Composable
fun ProductPopup(
    isVisible: Boolean,
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    onGoToCart: () -> Unit,
    isLoggedIn: Boolean,
    placeholderRes: Int = R.drawable.outline_add_shopping_cart_24
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var addedToCart by remember(isVisible) { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
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
                    .fillMaxWidth(0.94f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    ProductImage(
                        imageUrl = product.imageUrl,
                        placeholderRes = placeholderRes,
                        height = 180.dp
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = product.name,
                        style = typography.titleLarge.copy(
                            color = colors.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    if (product.category.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        CategoryBadge(
                            text = product.category,
                            backgroundColor = colors.secondaryContainer,
                            textColor = colors.onSecondaryContainer
                        )
                    }

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

                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = money(product.price),
                        style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                    )

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

                    Spacer(Modifier.height(24.dp))

                    // ✅ Ahora los botones están dentro del scroll
                    ActionButtons(
                        onAddToCart = {
                            onAddToCart()
                            addedToCart = true
                        },
                        onBuyNow = onBuyNow,
                        onGoToCart = onGoToCart,
                        addedToCart = addedToCart,
                        isLoggedIn = isLoggedIn
                    )
                }
            }

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
    onGoToCart: () -> Unit,
    addedToCart: Boolean,
    isLoggedIn: Boolean
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
                enabled = isLoggedIn,
                modifier = Modifier
                    .weight(0.4f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryContainer)
            ) {
                Text(
                    text = if (addedToCart) "Añadido" else "A la cesta",
                    style = typography.labelLarge.copy(
                        color = colors.onPrimaryContainer,
                        fontSize = 10.sp
                        )
                )
            }

            Button(
                onClick = onBuyNow,
                enabled = isLoggedIn,
                modifier = Modifier
                    .weight(0.4f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.secondary)
            ) {
                Text(
                    text = "Comprar",
                    style = typography.labelLarge.copy(
                        color = colors.onSecondary,
                        fontSize = 10.sp
                    )
                )
            }
        }

        Button(
            onClick = onGoToCart,
            enabled = isLoggedIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primaryContainer)
        ) {
            Text(
                text = "Ir al carrito",
                style = typography.labelLarge.copy(
                    color = colors.onPrimaryContainer,
                    fontSize = 10.sp
                )
            )
        }
    }
}
