/**
 * Barra de navegación inferior con iconos y badges para la cesta.
 *
 * Esta barra permite la navegación entre las pantallas principales de la aplicación:
 * - Info
 * - Productos
 * - Cesta (requiere login)
 * - Compra (requiere login)
 *
 * La cesta muestra un badge con el número de items actualmente en el carrito.
 *
 * @file BottomNavBar.kt
 */
package com.example.reto1_dam_2025_26.userInt.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.reto1_dam_2025_26.viewmodels.CartViewModel

/**
 * Composable para mostrar la barra de navegación inferior con iconos y badges.
 *
 * @param navController Controlador de navegación para gestionar rutas.
 * @param cartViewModel ViewModel para obtener el estado actual del carrito.
 * @param isLoggedIn Indica si el usuario está autenticado.
 * @param isLandscape Indica si la orientación del dispositivo es horizontal para ajustar el tamaño.
 */
@Composable
fun BottomNavBar(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    isLoggedIn: Boolean,
    isLandscape: Boolean // puedes quitarlo si ya no lo necesitas
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val cartItems = cartViewModel.items

    NavigationBar(
        // Fondo y contenido tomados de surface → contraste bueno con onSurface
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val navItems = listOf(
            NavItem("info", Icons.Filled.Info, "Info"),
            NavItem("productos", Icons.Filled.Menu, "Productos"),
            NavItem("cesta", Icons.Filled.ShoppingCart, "Cesta", requiresLogin = true),
            NavItem("compra", Icons.Filled.Search, "Compra", requiresLogin = true)
        )

        navItems.forEach { item ->
            val selected = currentRoute == item.route
            val isCart = item.route == "cesta"

            NavigationBarItem(
                icon = {
                    if (isCart && cartItems.isNotEmpty()) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ) { Text(cartItems.size.toString()) }
                            }
                        ) { Icon(item.icon, contentDescription = item.label) }
                    } else {
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = { Text(item.label, fontSize = 11.sp) },
                selected = selected,
                onClick = {
                    if ((!item.requiresLogin || isLoggedIn) && currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    // Icono/label cuando está seleccionado
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    // Icono/label cuando NO está seleccionado
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    // Color del “pill”/indicador bajo el item seleccionado
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

/**
 * Representa un elemento de navegación en la barra inferior.
 *
 * @property route Ruta asociada a este elemento para la navegación.
 * @property icon Icono que se mostrará en la barra de navegación.
 * @property label Texto descriptivo que aparece debajo del icono.
 * @property requiresLogin Indica si se requiere que el usuario esté autenticado para acceder a esta ruta.
 */
private data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val requiresLogin: Boolean = false
)