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

@Composable
fun BottomNavBar(
    navController: NavHostController,
    isLoggedIn: Boolean,
    isLandscape: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = if (isLandscape) Modifier.height(60.dp) else Modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        val navItems = listOf(
            NavItem("info", Icons.Default.Info, "Info"),
            NavItem("productos", Icons.Default.Menu, "Productos"),
            NavItem("cesta", Icons.Default.ShoppingCart, "Cesta", requiresLogin = true),
            NavItem("compra", Icons.Default.Search, "Compra", requiresLogin = true)
        )

        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    if (!item.requiresLogin || isLoggedIn) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

private data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val requiresLogin: Boolean = false
)
