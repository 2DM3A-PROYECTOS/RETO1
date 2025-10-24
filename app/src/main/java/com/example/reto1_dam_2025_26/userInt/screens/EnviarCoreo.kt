/**
 * Funciones para enviar un correo electrónico mediante una función Lambda HTTP.
 *
 * Proporciona la función [enviarCorreoLambda] que construye y envía una petición HTTP POST
 * con un JSON que incluye los campos "to", "subject" y "message".
 *
 * La petición se envía a una URL de Lambda definida y con un token de autenticación.
 * El envío se realiza en un hilo separado para no bloquear la interfaz principal.
 *
 * **Nota:** Cambiar la URL `url` y el token `token` por los valores correctos antes de usar.
 *
 * @file EnviarCoreo.kt
 */
package com.example.reto1_dam_2025_26.userInt.screens

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Envía un correo electrónico a través de una función Lambda HTTP.
 *
 * Construye un JSON con los campos:
 * - "to": destinatario del correo
 * - "subject": asunto del correo
 * - "message": cuerpo del correo
 *
 * Y lo envía en una petición POST a la URL definida.
 *
 * El envío se realiza en un hilo separado para no bloquear el hilo principal.
 *
 * **Importante:** Actualizar la URL y el token con los valores reales antes de usar.
 */
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