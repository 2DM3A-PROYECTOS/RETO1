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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.userInt.screens.Product

@Composable
fun ProductPopup(
    isVisible: Boolean, //  Controla si el popup se ve o
    product: Product? = null, // el producto selecionado
    onDismiss: () -> Unit, //  Acción al cerrar el popup (normalmente ocultarlo)
    onAddToCart: () -> Unit, //  Acción al pulsar "Añadir a carrito"
    onBuyNow: () -> Unit,  //  Acción al pulsar "Comprar ahora"
    onGoToCart: () -> Unit //  Acción al pulsar "Ir al carrito"
) {
    AnimatedVisibility(
        visible = isVisible, //  Solo se muestra si isVisible = true
        enter = fadeIn(), //  Animación de aparición
        exit = fadeOut() //  Animación de desaparición
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))  //  Fondo oscuro semitransparente
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // El cuadro del popup (tarjeta principal)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .clip(RoundedCornerShape(28.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF36C5C))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Url del imagen
                        AsyncImage(
                            model = product?.imageUrl?:"https://img.freepik.com/vector-gratis/ilustracion-vectorial-diseno-grafico_24908-54512.jpg",
                            contentDescription = product?.name?: "Imagen del producto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        //Nombre del producto
                        Text(
                            text = product?.name ?:"Nombre no disponible",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        //Descripción genérica
                        Text(
                            text =  "Producto de excelente calidad, disponible en nuestra tienda.",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        //Precio del producto
                        Text(
                            text = product?.price ?:"-",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(80.dp)) // место для кнопок
                    }

                    // ActionButtons siempre abajo
                    //Los botones (añadir, comprar, ir al carrito)
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
            //  Botón de cerrar (la “X”)
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 8.dp, y = 8.dp)
                    .size(36.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ActionButtons(
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    onGoToCart: () -> Unit
) {
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Añadir a carrito",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFF36C5C)
                )
            }

            Button(
                onClick = onBuyNow,
                modifier = Modifier
                    .weight(0.4f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF534F))
            ) {
                Text(
                    text = "Comprar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Button(
            onClick = onGoToCart,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                text = "Ir a carrito",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFF36C5C)
            )
        }
    }
}
