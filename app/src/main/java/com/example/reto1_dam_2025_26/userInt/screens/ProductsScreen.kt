/**
 * Pantalla y componentes para mostrar productos organizados por categorías,
 * permitir la visualización de detalles, añadir productos al carrito y gestionar
 * la navegación en la aplicación.
 *
 * Contiene la definición de [ProductsScreen], [CategorySection], [ProductCard]
 * y otras funciones auxiliares para la UI de productos.
 *
 * @file ProductsScreen.kt
 */
package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import coil.compose.AsyncImage
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.data.model.Product
import com.example.reto1_dam_2025_26.userInt.components.ProductPopup
import java.text.NumberFormat
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.reto1_dam_2025_26.viewmodels.ProductsViewModel
import com.example.reto1_dam_2025_26.viewmodels.CartViewModel

private val currencyLocale = Locale.forLanguageTag("es-ES")
private fun money(v: Double) = NumberFormat.getCurrencyInstance(currencyLocale).format(v)

/**
 * Pantalla principal que muestra los productos disponibles organizados por categorías.
 *
 * Obtiene la lista de productos desde [ProductsViewModel], gestiona el estado de carga,
 * error, y muestra una lista de categorías con sus productos correspondientes.
 *
 * También maneja la lógica para mostrar un popup con detalles del producto seleccionado,
 * permite añadir productos al carrito a través del [CartViewModel], y controla la navegación
 * a la pantalla de compra o carrito.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param cartViewModel ViewModel que maneja el estado del carrito de compra.
 * @param isLoggedIn Estado que indica si el usuario está autenticado.
 * @return Composable que representa la UI de la pantalla de productos.
 */
@Composable
fun ProductsScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    isLoggedIn: MutableState<Boolean>
) {
    // 1) Obtener VM de productos y estado
    val vm: ProductsViewModel = viewModel()
    val ui = vm.uiState.collectAsState().value

    // 2) Estado del popup
    var showPopup by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            ui.loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            ui.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error al cargar productos:\n${ui.error}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    FilledTonalButton(onClick = { vm.load() }) { Text("Reintentar") }
                }
            }

            ui.categories.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos disponibles.")
                }
            }

            else -> {
                // 3) Mostrar categorías y productos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ui.categories.forEach { (title, list) ->
                        item {
                            CategorySection(
                                title = title,
                                products = list,
                                onAddClick = { product ->
                                    cartViewModel.add(product) // añade al carrito
                                },
                                onOpen = { p ->
                                    selectedProduct = p
                                    showPopup = true
                                }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }

                // 4) Popup del producto seleccionado
                if (showPopup && selectedProduct != null) {
                    val p = selectedProduct!!

                    ProductPopup(
                        isVisible = true,
                        product = p,
                        onDismiss = { showPopup = false },
                        onAddToCart = {
                            cartViewModel.add(p)   // Añade al carrito desde el popup
                            showPopup = true       // Mantenemos visible el popup para ver el cambio de texto
                        },
                        onBuyNow = {
                            cartViewModel.add(p)   // Añade al carrito al comprar
                            showPopup = false
                            navController.navigate("compra") {
                                popUpTo("productos") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onGoToCart = {
                            showPopup = false
                            navController.navigate("cesta") {
                                popUpTo("productos") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        isLoggedIn = isLoggedIn.value
                    )
                }
            }
        }
    }
}

/**
 * Sección que muestra una categoría con su título y una fila horizontal
 * de tarjetas de productos disponibles.
 *
 * Calcula dinámicamente el ancho de cada tarjeta para mostrar 3 ítems visibles por fila,
 * ajustándose al tamaño de pantalla.
 *
 * @param title Título de la categoría (ejemplo: "Carnicería").
 * @param products Lista de productos pertenecientes a esta categoría.
 * @param modifier Modificador para ajustar la apariencia o comportamiento de la sección.
 * @param onAddClick Callback que se ejecuta al añadir un producto al carrito.
 * @param onOpen Callback que se ejecuta al abrir el detalle de un producto.
 * @return Composable que representa la UI de una sección de categoría con sus productos.
 */
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

/**
 * Tarjeta que muestra la información básica de un producto,
 * incluyendo imagen, nombre y precio.
 *
 * La tarjeta es clicable para abrir el detalle del producto y permite
 * añadirlo al carrito mediante callbacks.
 *
 * @param product Producto a mostrar en la tarjeta.
 * @param modifier Modificador para ajustar la apariencia o comportamiento de la tarjeta.
 * @param onAddClick Callback que se ejecuta al añadir el producto al carrito.
 * @param onOpen Callback que se ejecuta al abrir el detalle del producto.
 * @return Composable que representa la UI de una tarjeta de producto.
 */
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
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
