package InsertarBD

import com.google.firebase.firestore.FirebaseFirestore

class AñadirOrder {
    data class Order(
        val id: Int,
        val productos: List<AñadirProduct> ,
        val cantidad: Int,
    )
    fun  añadirOrder(){
        val db = FirebaseFirestore.getInstance()

        val  orders = listOf(
            Order(1,listOf(),0)
        )
    }
}