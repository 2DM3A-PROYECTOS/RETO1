package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.userInt.components.ProductPopup

//  Modelo de datos para los productos
data class Product(
    val name: String,
    val price: String,
    val imageUrl: String //he puesto los imagenes en string para poder meter el enlace
)

@Composable
fun ProductsScreen(navController: NavController) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    //  Listas de productos (puedes añadir más fácilmente)
    val carnes = listOf(
        Product("Filete de lomo", "10$", "https://nanafood.es/wp-content/uploads/2022/03/filete-lomo-cerdo-organico-nana-food-web.jpg"),
        Product("Chuletillas de cordero", "8$", "https://bistrobadia.de/wp-content/uploads/2024/04/lammkotelett-13.jpg"),
        Product("chorizo", "7.5$", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRieCUzOiCOF3d1ZsgdqiAvTQAL-z3I7jD2aw&s"),
        Product("Pechuga de pollo", "6$", "https://mejorconsalud.as.com/wp-content/uploads/2018/04/dos-pechugas-de-pollo.jpg?auto=webp&quality=7500&width=1920&crop=16:9,smart,safe&format=webp&optimize=medium&dpr=2&fit=cover&fm=webp&q=75&w=1920&h=1080"),
        Product("Chuleton", "64.5$", "https://tienda.hostalrioara.com/wp-content/uploads/2020/06/chuleton-de-ternera.jpg")

    )

    val pescados = listOf(
        Product("Merluza", "11.5", "https://imag.bonviveur.com/merluza-a-la-plancha.jpg"),
        Product("Bacalao", "10.5$", "https://www.lavanguardia.com/files/image_449_220/files/fp/uploads/2020/08/12/5f33f6624d0ca.r_d.2788-2025-916.jpeg"),
        Product("Bonito", "12$", "https://www.conxemar.com/wp-content/uploads/2022/12/sarda_chiliensis_el.jpeg"),
        Product("Salmon", "15.8$", "https://pescadosymariscos.consumer.es/sites/pescadosymariscos/files/styles/pagina_cabecera_desktop/public/2025-05/salmon_0.webp?h=6b0af028&itok=EtLAz0gR"),
        Product("Sardinas", "11.6$", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-2_1bEhTT5u2DkJa9pybOt1QXVL2IKAVftg&s")

    )

    val bebidas = listOf(
        Product("Zumo de naranja", "5.6$", "https://www.dia.es/product_images/50690/50690_ISO_0_ES.jpg"),
        Product("Coca Cola", "2.2", "https://tucervezaadomicilio.com/wp-content/uploads/2020/07/lata-coca-cola.jpg"),
        Product("Red bool", "3.1$", "https://www.dia.es/product_images/87367/87367_ISO_0_ES.jpg"),
        Product("Agua", "1.5$", "https://m.media-amazon.com/images/I/61mTu-foNxL.jpg"),
        Product("Pepsi", "2.1$", "https://www.confisur.es/865-medium_default/pepsi-cola-lata-33cl.jpg")

    )
    val pasteles = listOf(
        Product("Pastel vasco", "24$", "https://labasedelapasteleria.com/wp-content/uploads/pastel-vasco-tradicional-como-hacer.jpg"),
        Product("Tarta Pepisandwich", "31.5$", "https://www.pepinapastel.es/wp-content/uploads/2023/06/Tarta-sandwich-1-Cuad-scaled.jpg"),
        Product("tarta de queso", "8$", "https://i.blogs.es/6ad7a5/tarta-de-queso-philadelphia2/450_1000.jpg"),
        Product("Caja Pepiboms", "9.6$", "https://www.pepinapastel.es/wp-content/uploads/2020/11/PB3-C-scaled.jpg"),
        Product("Tarta Explosion de chocolate", "5.9$", "https://albayzin2020.com/wp-content/uploads/2021/07/20-scaled.jpg")
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
            onGoToCart = { navController.navigate("ShoppingCartScreen") }
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
        AsyncImage(
            model = product.imageUrl, //Url de la imagen
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


