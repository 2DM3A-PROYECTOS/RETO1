package InsertarBD

import com.example.reto1_dam_2025_26.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore


class DbProduct {

    private val db = FirebaseFirestore.getInstance()

    fun createProduct(product: Product, onDone: (Boolean, String?) -> Unit = {_,_->}) {
        db.collection("products") // 👈 unificado a "products"
            .document()           // autogenera ID
            .set(product)
            .addOnSuccessListener { onDone(true, null) }
            .addOnFailureListener { e -> onDone(false, e.message) }
    }

    /** Inserta una lista completa (para “sembrar” datos de prueba) */
    fun seed(list: List<Product>, onDone: (Int) -> Unit = {}) {
        var ok = 0
        list.forEach { p ->
            createProduct(p) { success, _ -> if (success) ok++ }
        }
        onDone(ok)
    }
}