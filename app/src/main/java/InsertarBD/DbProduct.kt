package InsertarBD

import com.example.reto1_dam_2025_26.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore


class DbProduct {

    /**
     * Crea un producto en la base de datos
     */
    fun createProduct(producto: Product){
        val db = FirebaseFirestore.getInstance()
        db.collection("Productos")
            .document()
            .set(producto)
            .addOnSuccessListener {
                println("Los Productos se han insertado con exito ")
            }
            .addOnFailureListener { e ->
                println("Ha producido un error")
            }
    }
}