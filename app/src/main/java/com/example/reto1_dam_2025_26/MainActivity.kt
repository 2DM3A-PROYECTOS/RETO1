package com.example.reto1_dam_2025_26

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reto1_dam_2025_26.interfaces.AccesoAplicacion
import com.example.reto1_dam_2025_26.interfaces.ConfirmacionContrasena
import com.example.reto1_dam_2025_26.ui.theme.Reto1_DAM_202526Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Reto1_DAM_202526Theme (dynamicColor = false){
                Surface {
                    gestorVentanas()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gestorVentanas() {

    val navController = rememberNavController()

    // Observar la ruta actual para cambiar el título dinámicamente
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "acceso"

    // estado de login para poder acceder a la barra inferior
    var isLoggedIn = remember { mutableStateOf(false) }

    // da acceso a la configuración actual del dispositivo para saber si está en horizontal o vertical
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(

        // barra superior
        topBar = {
            TopAppBar(
                modifier = if (isLandscape) Modifier.height(70.dp) else Modifier,
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        when (currentRoute) {
                            "acceso" -> "Mercado de la Ribera / Acceso"
                            "confirmacion" -> "Mercado de la Ribera"
                            else -> "App"
                        },
                        style = MaterialTheme.typography.titleMedium
                    )
                },
            )
        },

        // barra inferior
        bottomBar = {
            BottomAppBar(
                modifier = if (isLandscape) Modifier.height(60.dp) else Modifier,
                containerColor = MaterialTheme.colorScheme.secondary,
            ) {
                BottomNavBar(navController, isLoggedIn.value)
            }
        }
    )

    // cuerpo central de la pantalla
    { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
        ) {
            // definición de rutas de pantallas
            NavHost(navController = navController,
                startDestination = "acceso",
                modifier = Modifier
                    .padding(innerPadding) // el contenido no queda tapado por las barras
                    .padding(horizontal = 16.dp, vertical = 30.dp)
            ) {
                composable("acceso") {
                    AccesoAplicacion(navController, isLoggedIn)
                }

                composable("confirmacion") {
                    ConfirmacionContrasena(navController)
                }

            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController, isLoggedIn: Boolean) {
    // variables para poder cambiar de color los items al ser seleccionados
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar (containerColor = MaterialTheme.colorScheme.secondary){

        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "App") },
            selected = currentRoute == "app",
            onClick = {
                // solo se accede si el usuario se ha loggeado
                if (isLoggedIn) {
                    navController.navigate("app") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text("App") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Lista") },
            selected = currentRoute == "lista",
            onClick = {
                if (isLoggedIn) {
                    navController.navigate("lista") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text("Lista apps") },
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