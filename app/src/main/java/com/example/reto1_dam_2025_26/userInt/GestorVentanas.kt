package com.example.reto1_dam_2025_26.userInt

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.reto1_dam_2025_26.interfaces.*
import com.example.reto1_dam_2025_26.objetos.*
import com.example.reto1_dam_2025_26.viewmodels.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestorVentanas() {
    val navController = rememberNavController()

    var usuario by remember { mutableStateOf(Usuario("", "")) }
    var producto by remember { mutableStateOf(Producto("", "", 0.0, 21)) }

    val usuarioViewModel: UsuarioViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val pedidoViewModel: PedidoViewModel = viewModel()

    val isLoggedIn = remember { mutableStateOf(false) }

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
                title = { Text("Mercado de la Ribera", style = MaterialTheme.typography.titleMedium) },
                actions = {
                    Button(onClick = { /* TODO: Acción Log in */ }) {
                        Text("Log in", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController, isLoggedIn.value, isLandscape)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 30.dp)
        ) {
            NavHost(navController = navController, startDestination = "inicio") {
                composable("inicio") { InicioInterface(navController) }
                composable("catalogo") { CatalogoInterface(navController) }
                composable("loggin") { LogginInterface(navController) }
                composable("cesta") { CestaInterface(navController) }
                composable("pedidos") { PedidoInterface(navController) }
            }
        }
    }
}
