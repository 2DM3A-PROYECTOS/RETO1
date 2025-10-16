package InsertarBD

import com.google.firebase.firestore.FirebaseFirestore

class AñadirProduct {
    data class Producto(
        val id: Int = 0,
        val name: String = "",
        val descripcion: String = "",
        val sector: String = "",
        val store_name: String = "",
        val precio: Double = 0.0,
        val url_imegen: String = ""
    )

    fun añadirProduct(){
        val db = FirebaseFirestore.getInstance()

        val productos = listOf(
        Producto(1, "producto1","Este es producto 1","Pescado","Mercado Elena",15.6,""),
        Producto(2, "producto2","Este es producto 2","Carne","Mercado Fredi",25.6,""),
        Producto(3, "producto3","Este es producto 3","Bebida","Erosci",5.6,"")
        )
        for (producto in productos){
            db.collection("Productos")
                .document(producto.id.toString())
                .set(producto)
                .addOnSuccessListener {
                    println("Los Productos se han insertado con exito ")
                }
                .addOnFailureListener {
                    println("Ha producido un error")
                }
        }

    }
}