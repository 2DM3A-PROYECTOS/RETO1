package com.example.reto1_dam_2025_26.data.repository

import android.os.Handler
import android.os.Looper
import com.example.reto1_dam_2025_26.data.model.Order
import com.example.reto1_dam_2025_26.data.model.OrderItem
import com.example.reto1_dam_2025_26.data.model.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executors
import com.google.firebase.firestore.FieldValue


class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val io = Executors.newSingleThreadExecutor()
    private val main = Handler(Looper.getMainLooper())

    // ---------- AUTH ----------
    fun registerEmail(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        io.execute {
            try {
                Tasks.await(auth.createUserWithEmailAndPassword(email, password))
                main.post { onResult(true, null) }
            } catch (e: Exception) {
                main.post { onResult(false, e.message) }
            }
        }
    }

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
                main.post { onResult(false, e.message) }
            }
        }
    }

    fun addUserManager(
        email: String,
        password: String,
        username: String,
        address: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        io.execute {
            try {
                val user = hashMapOf(
                    "id" to (auth.currentUser?.uid ?: ""),
                    "username" to username,
                    "email" to email,
                    "address" to address,
                    "orders" to null
                )
                Tasks.await(db.collection("users").add(user))
                main.post { onResult(true, null) }
            } catch (e: Exception) {
                main.post { onResult(false, e.message) }
            }
        }
    }

    fun currentUid(): String? = auth.currentUser?.uid

    // ---------- PRODUCTS ----------
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

    // ---------- ORDERS ----------
    /**
     * Inserta order en base de datos order
     * Inserta al user en su lista de orderr la id de la order que ha generado
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
/*
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
                Tasks.await(ref.set(order))
                main.post { onResult(true, null, ref.id) }
            } catch (e: Exception) { main.post { onResult(false, e.message, null) } }
        }
    }
}*/
