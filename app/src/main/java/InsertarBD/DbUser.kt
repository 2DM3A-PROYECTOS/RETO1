package InsertarBD

import com.google.firebase.firestore.FirebaseFirestore

/**
 * Se encarga de conectarse a la base de datos y meter nuevos usuarios
 */
class DbUser {
    /**
     * Crea un usauario en la base de datos
     */
    fun createUser(user : User){
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .document(user.id.toString())
            .set(user)
            .addOnSuccessListener {
                println("User insertado correctomente: ${user.username} (${user.id})")
            }
            .addOnFailureListener { e ->
                println("Error al insertar: ${user.username}: $e")
            }
    }
}