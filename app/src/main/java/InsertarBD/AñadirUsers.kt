package InsertarBD

import com.google.firebase.firestore.FirebaseFirestore

class AñadirUsers {
    data class User(
        val id: Int = 0,
        val username: String = "",
        val password: String = "",
        val address: String = "",
        val email: String = "",
        val order: String =""
    )

    fun añadirUser(){
        val db = FirebaseFirestore.getInstance()

        val users = listOf(
            User(1, "Anthony","1234","Pipito FLores 4, 5izq","anthony@gmail.com",""),
            User(2, "Lucas","5678","Pipito FLores 4, 5izq","lucas@gmail.com",""),
            User(3, "Nikolai","9874","Pipito FLores 4, 5izq","nikolai@gmail.com","")
        )
        for (user in users){
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
}