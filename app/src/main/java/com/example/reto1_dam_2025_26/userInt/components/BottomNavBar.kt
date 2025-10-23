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

@Composable
fun BottomNavBar(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    isLoggedIn: Boolean,
    isLandscape: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val cartItems = cartViewModel.items

    NavigationBar(
        modifier = if (isLandscape) Modifier.height(60.dp) else Modifier.height(120.dp),
        containerColor = MaterialTheme.colorScheme.primary
    )
    {
        val navItems = listOf(
            NavItem("info", Icons.Default.Info, "Info"),
            NavItem("productos", Icons.Default.Menu, "Productos"),
            NavItem("cesta", Icons.Default.ShoppingCart, "Cesta", requiresLogin = true),
            NavItem("compra", Icons.Default.Search, "Compra", requiresLogin = true)
        )
        navItems.forEach { item ->
            val isCart = item.route == "cesta"

            NavigationBarItem(
                icon = {
                    if (isCart) {
                        // ✅ Icono con badge (por ejemplo, número 1)
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color.Red, // o MaterialTheme.colorScheme.error
                                    contentColor = Color.White
                                ) {
                                    Text(cartItems.size.toString())
                                }
                            }
                        ) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    } else {
                        // Icono normal para los demás
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = { Text(item.label, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    if (!item.requiresLogin || isLoggedIn) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent
                )
            )
        }

        /*
                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontSize = 10.sp) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (!item.requiresLogin || isLoggedIn) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            }
                        },

                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = Color.Transparent
                        )
                    )
                }*/
    }
}

private data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val requiresLogin: Boolean = false
)
