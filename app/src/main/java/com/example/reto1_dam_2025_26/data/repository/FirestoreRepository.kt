/**
 * Repositorio para gestionar operaciones con Firestore y Firebase Authentication.
 *
 * Proporciona métodos para autenticación, manejo de usuarios, productos y órdenes en la base de datos Firestore.
 *
 * @file FirestoreRepository.kt
 */
package com.example.reto1_dam_2025_26.data.repository

import android.os.Handler
import android.os.Looper
import com.example.reto1_dam_2025_26.data.model.Order
import com.example.reto1_dam_2025_26.data.model.OrderItem
import com.example.reto1_dam_2025_26.data.model.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executors
import com.google.firebase.firestore.FieldValue

/**
 * Clase que abstrae las operaciones de acceso a datos en Firestore y autenticación Firebase.
 */
class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val io = Executors.newSingleThreadExecutor()
    private val main = Handler(Looper.getMainLooper())

    /**
     * Traduce errores de FirebaseAuth en mensajes amigables para el usuario.
     *
     * @param e Excepción capturada.
     * @return Mensaje amigable para mostrar.
     */
    private fun getFriendlyErrorMessage(e: Exception): String {
        return when (e) {
            is FirebaseAuthException -> when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "El correo electrónico no es válido."
                "ERROR_WRONG_PASSWORD" -> "La contraseña es incorrecta."
                "ERROR_USER_NOT_FOUND" -> "No se encontró una cuenta con este correo."
                "ERROR_USER_DISABLED" -> "Esta cuenta ha sido deshabilitada."
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Este correo electrónico ya está en uso."
                "ERROR_WEAK_PASSWORD" -> "La contraseña es demasiado débil. Usa al menos 6 caracteres."
                else -> "Error de autenticación: ${e.message}"
            }
            else -> e.message ?: "Ocurrió un error desconocido."
        }
    }

    /**
     * Realiza el login con email y contraseña.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param onResult Callback con resultado: éxito (Boolean) y mensaje de error (String?).
     */
    fun loginEmail(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        io.execute {
            try {
                Tasks.await(auth.signInWithEmailAndPassword(email, password))
                main.post { onResult(true, null) }
            } catch (e: Exception) {
                val cause = (e.cause as? FirebaseAuthException) ?: e
                val friendly = getFriendlyErrorMessage(cause)
                main.post { onResult(false, friendly) }
            }
        }
    }

    /**
     * Registra un nuevo usuario con email y contraseña.
     *
     * @param email Correo electrónico a registrar.
     * @param password Contraseña para la cuenta.
     * @param onResult Callback con resultado: éxito (Boolean), mensaje de error (String?) y UID del usuario creado (String?).
     */
    fun registerEmail(
        email: String,
        password: String,
        onResult: (Boolean, String?, String?) -> Unit
    ) {
        io.execute {
            try {
                val result = Tasks.await(auth.createUserWithEmailAndPassword(email, password))
                val uid = result.user?.uid
                main.post { onResult(true, null, uid) }
            } catch (e: Exception) {
                val cause = (e.cause as? FirebaseAuthException) ?: e
                val friendly = getFriendlyErrorMessage(cause)
                main.post { onResult(false, friendly, null) }
            }
        }
    }

    /**
     * Agrega un usuario en Firestore tras la creación en FirebaseAuth.
     *
     * @param uid ID único del usuario.
     * @param email Correo electrónico del usuario.
     * @param username Nombre de usuario.
     * @param address Dirección del usuario.
     * @param onResult Callback con resultado: éxito (Boolean) y mensaje de error (String?).
     */
    fun addUserManager(
        uid: String,
        email: String,
        username: String,
        address: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        io.execute {
            try {
                val user = hashMapOf(
                    "id" to uid,
                    "username" to username,
                    "email" to email,
                    "address" to address,
                    "orders" to emptyList<String>()
                )
                Tasks.await(db.collection("users").document(uid).set(user))
                main.post { onResult(true, null) }
            } catch (e: Exception) {
                val friendly = getFriendlyErrorMessage(e)
                main.post { onResult(false, friendly) }
            }
        }
    }

    /**
     * Obtiene los datos de un usuario a partir de su UID.
     *
     * @param uid ID del usuario.
     * @param onResult Callback con resultado: mapa de datos (Map<String, Any>?) y mensaje de error (String?).
     */
    fun getUserData(
        uid: String,
        onResult: (Map<String, Any>?, String?) -> Unit
    ) {
        io.execute {
            try {
                val doc = Tasks.await(db.collection("users").document(uid).get())
                if (doc.exists()) {
                    main.post { onResult(doc.data, null) }
                } else {
                    main.post { onResult(null, "No se encontraron datos del usuario.") }
                }
            } catch (e: Exception) {
                val friendly = getFriendlyErrorMessage(e)
                main.post { onResult(null, friendly) }
            }
        }
    }

    /**
     * Obtiene el UID del usuario actualmente autenticado, o null si no hay sesión iniciada.
     *
     * @return UID del usuario o null.
     */
    fun currentUid(): String? = auth.currentUser?.uid

    /**
     * Obtiene la lista de todos los productos disponibles.
     *
     * @param onResult Callback con resultado: lista de productos (List<Product>?) y mensaje de error (String?).
     */
    fun getAllProducts(onResult: (List<Product>?, String?) -> Unit) {
        io.execute {
            try {
                val qs = Tasks.await(db.collection("products").get())
                val list = qs.toObjects(Product::class.java)
                main.post { onResult(list, null) }
            } catch (e: Exception) {
                main.post { onResult(null, e.message) }
            }
        }
    }

    /**
     * Crea una nueva orden en Firestore y actualiza la lista de órdenes del usuario.
     *
     * @param userId ID del usuario que realiza la orden.
     * @param items Lista de ítems comprados.
     * @param paymentMethod Método de pago utilizado.
     * @param shippingAddress Dirección de envío.
     * @param onResult Callback con resultado: éxito (Boolean), mensaje de error (String?) y ID de la orden creada (String?).
     */
    fun createOrder(
        userId: String,
        items: List<OrderItem>,
        paymentMethod: String,
        shippingAddress: String,
        onResult: (Boolean, String?, String?) -> Unit
    ) {
        io.execute {
            try {
                require(userId.isNotEmpty()) { "userId vacío" }
                require(items.isNotEmpty()) { "Sin items" }

                val total = items.sumOf { it.price * it.qty }
                val ref = db.collection("orders").document()
                val order = Order(
                    id = ref.id,
                    userId = userId,
                    items = items,
                    total = total,
                    status = "PENDING",
                    paymentMethod = paymentMethod,
                    shippingAddress = shippingAddress
                )

                // Guardar la orden
                Tasks.await(ref.set(order))

                // Actualizar el campo 'orders' del usuario
                val userRef = db.collection("users").document(userId)
                Tasks.await(userRef.update("orders", FieldValue.arrayUnion(ref.id)))

                // Resultado exitoso
                main.post { onResult(true, null, ref.id) }

            } catch (e: Exception) {
                main.post { onResult(false, e.message, null) }
            }
        }
    }
}