package com.example.reto1_dam_2025_26

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// crea un campo de texto
@Composable
fun CrearCampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    error: Boolean,
    modifier: Modifier = Modifier
) {
    val visualTransformation = if (labelText != "Nombre app") {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        singleLine = true,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(50),
        modifier = modifier
            .padding(horizontal = 30.dp, vertical = 8.dp)
            .fillMaxWidth(),
        isError = error,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary
        )
    )
}

// crea los colores de un botón
@Composable
fun CrearColoresBoton(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
}