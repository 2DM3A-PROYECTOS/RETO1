package com.example.reto1_dam_2025_26.userInt.screens

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun enviarCorreoLambda(toEmail: String = "alumno@example.com") {
    val url = "https://oeqwgxpckvlnl3u3vvrfai4t6a0pelmp.lambda-url.us-east-1.on.aws/"
    val token = "mF8Xk9u2l7OaB5FZqvH2j1sW9cR8yTzP6aQvB0xE3fI"

    val json = JSONObject().apply {
        put("to", toEmail)
        put("subject", "¡Registro exitoso!")
        put("message", "Bienvenido/a, tu cuenta ha sido creada correctamente 🎉")
    }

    val client = OkHttpClient()

    val requestBody = json.toString()
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("x-api-key", token) // ✅ correcto
        .build()

    Thread {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                println("✅ Correo enviado correctamente: ${response.body?.string()}")
            } else {
                println("❌ Error al enviar correo: ${response.code} -> ${response.body?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}
