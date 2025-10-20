package com.example.reto1_dam_2025_26.userInt.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.data.model.Product
import com.example.reto1_dam_2025_26.userInt.components.ProductPopup
import java.text.NumberFormat
import java.util.Locale

// ==== Helpers ====
private val currencyLocale = Locale.forLanguageTag("es-ES")
private fun money(v: Double) = NumberFormat.getCurrencyInstance(currencyLocale).format(v)

// ==== UI reusables ====
@Composable
private fun CategorySection(
    title: String,
    products: List<Product>,
    modifier: Modifier = Modifier,
    onAddClick: (Product) -> Unit = {},
    onOpen: (Product) -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Queremos 3 ítems visibles por "fila"
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val outerPadding = 16.dp * 2      // Padding lateral del LazyColumn (16 a cada lado)
    val rowContentPadding = 2.dp * 2  // contentPadding horizontal (izq+der) del LazyRow
    val itemSpacing = 16.dp           // espacio entre cards en LazyRow
    val visibleColumns = 3
    val totalSpacing = itemSpacing * (visibleColumns - 1)
    val available = screenWidth - outerPadding - rowContentPadding - totalSpacing
    val itemWidth = available / visibleColumns

    Column(modifier = modifier) {
        Text(
            text = title,
            style = typography.titleLarge,
            color = colors.onBackground
        )
        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(count = products.size, key = { i -> products[i].id }) { i ->
                ProductCard(
                    product = products[i],
                    modifier = Modifier.width(itemWidth), // 👈 ancho calculado
                    onAddClick = onAddClick,
                    onOpen = onOpen
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onAddClick: (Product) -> Unit,
    onOpen: (Product) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onOpen(product) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            // Imagen rectangular con bordes redondeados
            if (product.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = money(product.price),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            //Spacer(Modifier.height(6.dp))
//
            //FilledTonalButton(
            //    onClick = { onAddClick(product) },
            //    modifier = Modifier
            //        .fillMaxWidth()
            //        .height(36.dp)
            //) {
            //    Text("Añadir")
            //}
        }
    }
}



// ==== Pantalla ====
@Composable
fun ProductsScreen(
    navController: NavController,
    onAddClick: (Product) -> Unit = {} // conecta con tu CartViewModel.add(product)
) {
    // Estado del popup
    var showPopup by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    // ---- Demo data (hasta conectar Firestore) ----
    val carne = remember {
        listOf(
            Product(
                id = "carne_1", name = "Filete de lomo", description = "Corte fresco de lomo",
                price = 10.0, stock = 12, imageUrl = "",
                category = "Carne", storeName = "Carnicería Ribera"
            ),
            Product(
                id = "carne_2", name = "Chuletillas de cordero", description = "Tiernas y sabrosas",
                price = 8.0, stock = 9, imageUrl = "",
                category = "Carne", storeName = "Carnicería Ribera"
            ),
            Product(
                id = "carne_3", name = "Metro", description = "Corte especial",
                price = 7.5, stock = 7, imageUrl = "",
                category = "Carne", storeName = "Carnicería Ribera"
            ),
            Product(
                id = "carne_4", name = "Pechuga de pollo", description = "Magras y jugosas",
                price = 6.0, stock = 15, imageUrl = "",
                category = "Carne", storeName = "Carnicería Ribera"
            ),
            Product(
                id = "carne_5", name = "Chuletón", description = "Maduración 30 días",
                price = 64.5, stock = 3, imageUrl = "",
                category = "Carne", storeName = "Carnicería Ribera"
            )
        )
    }
    val pescado = remember {
        listOf(
            Product("pesc_1","Merluza","Del Cantábrico",11.5,8,"","Pescado","Pescadería Norte"),
            Product("pesc_2","Bacalao","Tradicional",10.5,10,"","Pescado","Pescadería Norte"),
            Product("pesc_3","Bonito","Temporada",12.0,6,"","Pescado","Pescadería Norte"),
            Product("pesc_4","Salmón","Noruego",15.8,5,"","Pescado","Pescadería Norte"),
            Product("pesc_5","Sardinas","Frescas",11.6,20,"","Pescado","Pescadería Norte"),
        )
    }
    val bebidas = remember {
        listOf(
            Product("beb_1","Zumo de naranja","Natural exprimido",5.6,30,"","Bebidas","Frutería Sol"),
            Product("beb_2","Coca-Cola","Lata 330ml",2.2,100,"","Bebidas","Autoservicio Rio"),
            Product("beb_3","Red Bull","Lata 250ml",3.1,50,"","Bebidas","Autoservicio Rio"),
            Product("beb_4","Agua","Botella 1.5L",1.5,200,"","Bebidas","Autoservicio Rio"),
            Product("beb_5","Pepsi","Lata 330ml",2.1,80,"","Bebidas","Autoservicio Rio"),
        )
    }
    val pasteles = remember {
        listOf(
            Product("pas_1","Pastel Vasco","Relleno de crema",24.0,4,"","Pasteles","Pastelería Dulce"),
            Product("pas_2","Tarta Pepisandwich","Chocolate y crema",31.5,3,"","Pasteles","Pastelería Dulce"),
            Product("pas_3","Topper Feliz Cumple","Topper decorativo",8.0,15,"","Pasteles","Pastelería Dulce"),
            Product("pas_4","Tarta Explosión de chocolate","Triple choco",5.9,10,"","Pasteles","Pastelería Dulce"),
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                CategorySection(
                    "Carne",
                    carne,
                    onAddClick = onAddClick,
                    onOpen = { p -> selectedProduct = p; showPopup = true }
                )
            }
            item {
                CategorySection(
                    "Pescado",
                    pescado,
                    onAddClick = onAddClick,
                    onOpen = { p -> selectedProduct = p; showPopup = true }
                )
            }
            item {
                CategorySection(
                    "Bebidas",
                    bebidas,
                    onAddClick = onAddClick,
                    onOpen = { p -> selectedProduct = p; showPopup = true }
                )
            }
            item {
                CategorySection(
                    "Pasteles",
                    pasteles,
                    onAddClick = onAddClick,
                    onOpen = { p -> selectedProduct = p; showPopup = true }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }

// Muestra el popup solo cuando hay producto seleccionado
        if (showPopup && selectedProduct != null) {
            val p = selectedProduct!!
            ProductPopup(
                isVisible = true,
                product = p,
                onDismiss = { showPopup = false },
                onAddToCart = {
                    onAddClick(p)
                    showPopup = false
                },
                onBuyNow = {
                    onAddClick(p)
                    showPopup = false
                    navController.navigate("ShoppingCartScreen")
                },
                onGoToCart = {
                    showPopup = false
                    navController.navigate("ShoppingCartScreen")
                }
            )
        }

    }
}



@Preview(showBackground = true)
@Composable
private fun ProductsScreenPreview() {
    val nav = rememberNavController()
    ProductsScreen(navController = nav)
}