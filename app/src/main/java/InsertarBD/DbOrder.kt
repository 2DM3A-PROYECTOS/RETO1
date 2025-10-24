package InsertarBD

import com.example.reto1_dam_2025_26.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 *
 */
class DbOrder {

    /**
     * Crea un pedido en la base de datos
     */
    suspend fun createOrder(producto: Product): String? {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Orders").document() // genera ID automáticamente
        val idOrder = docRef.id

        return try {
            docRef.set(producto).await() // espera a que se complete
            println("Order creado con ID: $idOrder")
            idOrder // devolvemos el ID con return (ya es seguro)
        } catch (e: Exception) {
            println("Error al crear producto: ${e.message}")
            null
        }
    }

    /**
     * Usando un identificador de order introduce un producto a ese pedido
     */
    suspend fun addProductToOrder(idOrder: String, product: Product): String? {
        val db = FirebaseFirestore.getInstance()

        //db.collection('users').doc(this.username).collection('booksList').doc(myBookId).set({
        //    password: this.password,
        //    name: this.name,
        //    rollno: this.rollno
        //})
        return  try{
        val DocRef = db.collection("Orders")
            .document(idOrder)
            .collection("Products")
            .document()

        val productId = DocRef.id

        DocRef.set(product).await()
        println("Producto se ha añadido con exito al pedido $idOrder, Id producto $productId")
        productId // retorna el ID del producto añadido
        } catch (e: Exception){
            println("Se ha producido un error")
            null
        }

    }
}
