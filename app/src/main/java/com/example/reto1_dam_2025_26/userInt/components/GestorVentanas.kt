/**
 * Archivo: GestorVentanas.kt
 *
 * Composable principal que gestiona la navegación y la estructura general
 * de la aplicación utilizando un [Scaffold] con barra superior y barra inferior.
 *
 * Esta función configura los [ViewModel]s necesarios, controla el estado de inicio de sesión,
 * detecta la orientación del dispositivo y administra la navegación entre las pantallas principales:
 * - Info
 * - Productos
 * - Cesta de compra
 * - Compra
 * - Pantalla de agradecimiento
 *
 * También adapta el tamaño de la barra superior y la barra inferior según la orientación (portrait o landscape).
 */
package com.example.reto1_dam_2025_26.userInt.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
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
import com.example.reto1_dam_2025_26.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestorVentanas(
    onLogout: () -> Unit = {} // ← callback para volver al login desde MainActivity
) {

    val userViewModel: UserViewModel = viewModel()
    val productViewModel: ProductsViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()

    // Estado de sesión visible para UI (BottomBar / botón logout)
    var isLoggedIn by remember {
        mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
    }

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
                    containerColor = RojoMercado,
                    titleContentColor = MaterialTheme.colorScheme.surface,
                    actionIconContentColor = MaterialTheme.colorScheme.surface // ← color del icono
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
                    // Texto contextual (lo conservamos)
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
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // -------- Icono de Logout (derecha) --------
                    if (isLoggedIn) {
                        IconButton(
                            onClick = {
                                // 1) Cerrar sesión Firebase
                                FirebaseAuth.getInstance().signOut()
                                isLoggedIn = false

                                // 2) Limpieza ligera (opcional)
                                cartViewModel.clear()

                                // 3) Volver al login (lo orquesta MainActivity con este callback)
                                onLogout()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Logout,
                                contentDescription = "Cerrar sesión"
                                // color ya es MaterialTheme.colorScheme.surface por actionIconContentColor
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                cartViewModel = cartViewModel,
                isLoggedIn = isLoggedIn,
                isLandscape = isLandscape
            )
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
            composable("productos") { ProductsScreen(navController, cartViewModel, remember { mutableStateOf(isLoggedIn) }) }
            composable("cesta") { ShoppingCartScreen(navController, cartViewModel) }
            composable("compra") { OrderScreen(navController, cartViewModel, userViewModel, orderViewModel) }
            composable("gracias") { Thanks(navController) }
        }
    }
}
