package com.example.reto1_dam_2025_26.db_consult

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

fun loginQuery(email: String, password: String, onResult: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val context: Context

    if (email.isEmpty() && password.isEmpty()) {
        Toast.makeText(context, "Por favor, ingrese su correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
        return
    }
}