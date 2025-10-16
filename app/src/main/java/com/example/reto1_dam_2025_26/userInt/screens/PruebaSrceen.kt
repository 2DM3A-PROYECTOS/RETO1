package com.example.reto1_dam_2025_26.userInt.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reto1_dam_2025_26.userInt.components.ProductPopup

@Composable
fun Es_Una_Prueba() {
    var showPopup by remember { mutableStateOf(false) }

    Button(
        onClick = {
            showPopup = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
    ) {
        Text("PRUEBA", color = Color.Black, fontWeight = FontWeight.Bold)
    }
    //ProductPopup(
    //    isVisible = showPopup,
    //    onDismiss = { showPopup = false },
    //    onAddToCart = { /* TODO: добавить в корзину из БД */ },
    //    onBuyNow = { /* TODO: оформить покупку */ },
    //    onGoToCart = { /* TODO: переход в корзину */ }
    //)
}