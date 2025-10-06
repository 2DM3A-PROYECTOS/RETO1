package com.example.reto1_dam_2025_26.interfaces

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reto1_dam_2025_26.CrearCampoTexto
import com.example.reto1_dam_2025_26.CrearColoresBoton

@Composable
fun AccesoAplicacion(navController: NavController, isLoggedIn: MutableState<Boolean>) {
    var contrasena by remember { mutableStateOf("") }
    var confirmacion by remember { mutableStateOf("") }
    var existeArchivo = false
    var error by remember { mutableStateOf(false) } // comprobación de confirmacion de contraseña

    // para cambiar el foco del cursor
    val focusRequesterContrasena = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // si no existe aun el archivo con la contraseña maestra
    if(existeArchivo == false) {
        LazyColumn ()
        {
            item {
                CrearCampoTexto(contrasena,
                    onValueChange = { contrasena = it },
                    "Contraseña",
                    error, modifier = Modifier.focusRequester(focusRequesterContrasena))
            }

            item {
                CrearCampoTexto(confirmacion,
                    onValueChange = { confirmacion = it },
                    "Repetir contraseña",
                    error)
            }
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth())
                {
                    Button(onClick = {
                        // si las contraseñas coinciden
                        if (contrasena == confirmacion) {
                            contrasena = "" // se limpian los campos
                            confirmacion = ""
                            isLoggedIn.value = true // se habilita la barra inferior
                            navController.navigate("confirmacion") // se navega a página confirmacion

                        } else {
                            contrasena = ""
                            confirmacion = ""
                            error = true
                            focusRequesterContrasena.requestFocus() // mueve el cursor al primer campo
                        }
                    },
                        colors = CrearColoresBoton(),
                        modifier = Modifier.padding(top = 26.dp)) {
                        Text("Crear")
                    }
                }

            }
        }
    } else {
        LazyColumn ()
        {
            confirmacion = "1" // aqui hay que introducir la contraseña maestra guardada en archivo

            item {
                Text("Confirma tu contraseña maestra")
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                CrearCampoTexto(contrasena,
                    onValueChange = { contrasena = it },
                    "Contraseña maestra",
                    error)
            }

            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth())
                {
                    Button(onClick = {
                        // si las contraseñas coinciden
                        if (contrasena == confirmacion) {
                            contrasena = "" // se limpian los campos
                            confirmacion = ""
                            isLoggedIn.value = true // se habilita la barra inferior
                            navController.navigate("confirmacion") // se navega a página confirmacion
                        } else {
                            contrasena = ""
                            error = true
                        }
                    },
                        colors = CrearColoresBoton(),
                        modifier = Modifier.padding(top = 26.dp)) {
                        Text("Confirmar")
                    }
                }
            }
        }
    }
}