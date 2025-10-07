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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reto1_dam_2025_26.interfaces.CatalogoInterface
import com.example.reto1_dam_2025_26.interfaces.CestaInterface
import com.example.reto1_dam_2025_26.interfaces.InicioInterface
import com.example.reto1_dam_2025_26.interfaces.LogginInterface
import com.example.reto1_dam_2025_26.interfaces.PedidoInterface
import com.example.reto1_dam_2025_26.objetos.Pedido
import com.example.reto1_dam_2025_26.objetos.Producto
import com.example.reto1_dam_2025_26.objetos.Usuario
import com.example.reto1_dam_2025_26.ui.theme.Reto1_DAM_202526Theme
import com.example.reto1_dam_2025_26.viewmodels.PedidoViewModel
import com.example.reto1_dam_2025_26.viewmodels.ProductoViewModel
import com.example.reto1_dam_2025_26.viewmodels.UsuarioViewModel

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

/**
 * Vista previa de la aplicación en el editor de diseño.
 */
@Preview
@Composable
fun AppPreview() {
    Reto1_DAM_202526Theme(dynamicColor = false) {
        Surface(
        ) {
            gestorVentanas()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gestorVentanas() {
    // NavController para navegar entre las páginas
    val navController = rememberNavController()
/*
    // Objetos
    var usuario by remember { mutableStateOf(Usuario("", "")) }
    var producto by remember { mutableStateOf(Producto("", "", 0.0, 21)) }
    var productos: List<Producto>
    //var pedido by remember { mutableStateOf(Pedido(usuario, productos)) }

    // View model de los objetos
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val pedidoViewModel: PedidoViewModel = viewModel()
*/
    // Observar la ruta actual para cambiar el título dinámicamente
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "inicio"

    // estado de login para poder acceder a la barra inferior
    var isLoggedIn = remember { mutableStateOf(false) }

    // da acceso a la configuración actual del dispositivo para saber si está en horizontal o vertical
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(

        topBar = {
            TopAppBar(
                modifier = if (isLandscape) Modifier.height(70.dp) else Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Mercado de la Ribera",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    Button(onClick = { /* Acción Log in */ }) {
                        Text("Log in", color = MaterialTheme.colorScheme.primary)
                    }
                }
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
                startDestination = "inicio",
                modifier = Modifier
                    .padding(innerPadding) // el contenido no queda tapado por las barras
                    .padding(horizontal = 16.dp, vertical = 30.dp)
            ) {
                composable("inicio") {
                    InicioInterface(navController)
                }
                composable("catalogo") {
                    CatalogoInterface(navController)
                }
                composable("loggin") {
                    LogginInterface(navController)
                }
                composable("cesta") {
                    CestaInterface(navController)
                }
                composable("pedidos") {
                    PedidoInterface(navController)
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

        // Inicio
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            selected = currentRoute == "inicio",
            onClick = {
                    navController.navigate("inicio") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }

            },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = Color.Transparent
            )
        )

        // Catálogo
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Catalogo") },
            selected = currentRoute == "catalogo",
            onClick = {
                navController.navigate("catalogo") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }

            },
            label = { Text("Catalogo") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = Color.Transparent
            )
        )

        // Cesta
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cesta") },
            selected = currentRoute == "cesta",
            onClick = {
                if (isLoggedIn) {
                    navController.navigate("cesta") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text("Cesta") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = Color.Transparent
            )
        )

        // Pedidos
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Pedidos") },
            selected = currentRoute == "pedidos",
            onClick = {
                if (isLoggedIn) {
                    navController.navigate("pedidos") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text("Pedidos") },
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