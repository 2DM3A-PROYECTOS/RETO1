package com.example.reto1_dam_2025_26.userInt.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.reto1_dam_2025_26.userInt.screens.InfoScreen
import com.example.reto1_dam_2025_26.viewmodels.*
import com.example.reto1_dam_2025_26.R
import com.example.reto1_dam_2025_26.userInt.screens.OrderScreen
import com.example.reto1_dam_2025_26.userInt.screens.ProductsScreen
import com.example.reto1_dam_2025_26.userInt.screens.ShoppingCartScreen
import com.example.reto1_dam_2025_26.userInt.screens.Thanks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestorVentanas() {

    val userViewModel: UserViewModel = viewModel()
    val productViewModel: ProductsViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()

    val isLoggedIn = remember { mutableStateOf(true) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,    // ← fondo global correcto
        topBar = {
            TopAppBar(
                modifier = if (isLandscape) Modifier.height(70.dp) else Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Mercado de la Ribera",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.height(56.dp) // altura típica de appbar
                    )
                },
                actions = {
                    val actionText = when (currentRoute) {
                        "info" -> "Info"
                        "productos" -> "Productos"
                        "cesta" -> "Cesta"
                        "compra" -> "Compra"
                        else -> ""
                    }
                    if (actionText.isNotEmpty()) {
                        Text(
                            text = actionText,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.padding(end = 16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController, isLoggedIn.value, isLandscape)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "info",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // solo respetar top/bottom bar
        ) {
            composable("info") { InfoScreen(navController) }
            composable("productos") { ProductsScreen(navController, cartViewModel) }
            composable("cesta") { ShoppingCartScreen(navController, cartViewModel ) }
            composable("compra") { OrderScreen(navController) }
            composable("gracias") { Thanks(navController) }
        }
    }
}

