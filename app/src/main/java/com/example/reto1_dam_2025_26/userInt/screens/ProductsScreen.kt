package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.userInt.components.ProductPopup

//  Modelo de datos para los productos
data class Product(
    val name: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun ProductsScreen(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    //  Listas de productos (puedes añadir más fácilmente)
    val carnes = listOf(
        Product("Filete de lomo", "10$", R.drawable.outline_add_shopping_cart_24),
        Product("Chuletillas de cordero", "8$", R.drawable.outline_add_shopping_cart_24),
        Product("Metro", "7.5$", R.drawable.outline_add_shopping_cart_24),
        Product("Pechuga de pollo", "6$", R.drawable.outline_add_shopping_cart_24),
        Product("Chuleton", "64.5$", R.drawable.outline_add_shopping_cart_24)

    )

    val pescados = listOf(
        Product("Merluza", "11.5", R.drawable.outline_add_shopping_cart_24),
        Product("Bacalao", "10.5$", R.drawable.outline_add_shopping_cart_24),
        Product("Bonito", "12$", R.drawable.outline_add_shopping_cart_24),
        Product("Salmon", "15.8$", R.drawable.outline_add_shopping_cart_24),
        Product("Sardinas", "11.6$", R.drawable.outline_add_shopping_cart_24)

    )

    val bebidas = listOf(
        Product("Zumo de naranja", "5.6$", R.drawable.outline_add_shopping_cart_24),
        Product("Coca Cola", "2.2", R.drawable.outline_add_shopping_cart_24),
        Product("Red bool", "3.1$", R.drawable.outline_add_shopping_cart_24),
        Product("Agua", "1.5$", R.drawable.outline_add_shopping_cart_24),
        Product("Pepsi", "2.1$", R.drawable.outline_add_shopping_cart_24)

    )
    val pasteles = listOf(
        Product("Pastel vasco", "24$", R.drawable.outline_add_shopping_cart_24),
        Product("Tarta Pepisandwich", "31.5$", R.drawable.outline_add_shopping_cart_24),
        Product("Topper Feliz Cumple", "8$", R.drawable.outline_add_shopping_cart_24),
        Product("Caja Pepiboms", "9.6$", R.drawable.outline_add_shopping_cart_24),
        Product("Tarta Explosion de chocolate", "5.9$", R.drawable.outline_add_shopping_cart_24)
    )


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CategoryRow("Carne", carnes) { product ->
                    selectedProduct = product
                    showPopup = true
                }
                Spacer(modifier = Modifier.height(24.dp))
                CategoryRow("Pescado", pescados) { product ->
                    selectedProduct = product
                    showPopup = true
                }
                Spacer(modifier = Modifier.height(24.dp))
                CategoryRow("Bebidas", bebidas) { product ->
                    selectedProduct = product
                    showPopup = true
                }
                Spacer(modifier = Modifier.height(24.dp))
                CategoryRow("Pasteles", pasteles) { product ->
                    selectedProduct = product
                    showPopup = true
                }
            }
        }

        //  Muestra el popup si se ha pulsado un producto
        ProductPopup(
            isVisible = showPopup,
            onDismiss = { showPopup = false },
            product = selectedProduct, // producto seleccionado
            onAddToCart = { /* Añadir al carrito */ },
            onBuyNow = { /* Comprar ahora */ },
            onGoToCart = { navController.navigate("cartScreen") }
        )
    }
}

//  Un componente para cada categoría (Carne, Pescado, etc.)
@Composable
fun CategoryRow(title: String, products: List<Product>, onProductClick: (Product) -> Unit) {
    Text(text = title, color = Color.Black, fontSize = 40.sp)
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products.size) { index ->
            ProductItem(product = products[index], onClick = { onProductClick(products[index]) })
        }
    }
}

// 🔹 Un componente reutilizable para cada producto
@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(product.imageRes),
            contentDescription = product.name,
            modifier = Modifier
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary)
                .size(100.dp)
                .clickable { onClick() }
        )
        Text(text = product.name, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.price, color = Color.Black)
    }
}


