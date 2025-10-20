package com.example.reto1_dam_2025_26.userInt.screens

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
fun enviarCorreoLambda() {
    val url = "https://TU_LAMBDA_URL" // La URL de tu Lambda
    val token = "mF8Xk9u2l7OaB5FZqvH2j1sW9cR8yTzP6aQvB0xE3fI"
    // Crear JSON con los campos requeridos
    val json = JSONObject()
    json.put("to", "alumno@example.com")
    json.put("subject", "Prueba desde Android")
    json.put("message", "Hola, esto es un correo de prueba desde la app")
    // Crear cliente HTTP
    val client = OkHttpClient()
    // Crear request
    val requestBody = json.toString().toRequestBody("application/json; charset=utf 8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("mF8Xk9u2l7OaB5FZqvH2j1sW9cR8yTzP6aQvB0xE3fI", token) // o el nombre del header correcto
        .build() // <-- esto debe ir fuera del comentario
    // Ejecutar request en un hilo secundario
    Thread {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                println("Correo enviado correctamente: ${response.body?.string()}")  } else {
                println("Error al enviar correo: ${response.code}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}
