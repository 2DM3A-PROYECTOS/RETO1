package InsertarBD

data class User(
    val id: Int = 0,
    val username: String = "",
    val password: String = "",
    val address: String = "",
    val email: String = "",
    val orders: List<Order> = emptyList<Order>(),
)

data class Order(
    val id: Int,
    val productos: List<Producto> = emptyList<Producto>(),
    val precio_total: Int,
)

data class Producto(
    val id: Int = 0,
    val name: String = "",
    val descripcion: String = "",
    val sector: String = "",
    val store_name: String = "",
    val precio: Double = 0.0,
    val url_imegen: String = ""
)