package com.example.reto1_dam_2025_26

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import InsertarBD.DbProduct
import com.example.reto1_dam_2025_26.data.model.Product

@RunWith(AndroidJUnit4::class)
class InsertDatabaseTest {

    @Test
    fun seed_products_in_firestore() {
        // Asegura que Firebase esté inicializado en el entorno de test
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        if (FirebaseApp.getApps(ctx).isEmpty()) FirebaseApp.initializeApp(ctx)

        val all = buildAllProducts()

        val db = DbProduct()
        val latch = CountDownLatch(all.size)
        val ok = AtomicInteger(0)

        all.forEach { p ->
            db.createProduct(p) { success, _ ->
                if (success) ok.incrementAndGet()
                latch.countDown()
            }
        }

        // Espera a que suban todos (ajusta el timeout si tu conexión es lenta)
        val completed = latch.await(60, TimeUnit.SECONDS)
        assertTrue("Timeout esperando inserciones", completed)
        assertEquals("No se insertaron todos los productos", all.size, ok.get())
    }

    // ---------- Datos de ejemplo (sin remember, apto para test) ----------
    private fun buildAllProducts(): List<Product> {
        val carne = listOf(
            Product("carne_1","Filete de lomo","Corte fresco de lomo",10.0,12,
                "https://nanafood.es/wp-content/uploads/2022/03/filete-lomo-cerdo-organico-nana-food-web.jpg",
                "Carne","Carnicería Ribera"),
            Product("carne_2","Chuletillas de cordero","Tiernas y sabrosas",8.0,9,
                "https://bistrobadia.de/wp-content/uploads/2024/04/lammkotelett-13.jpg",
                "Carne","Carnicería Ribera"),
            Product("carne_3","Chorizo","Corte especial",7.5,7,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRieCUzOiCOF3d1ZsgdqiAvTQAL-z3I7jD2aw&s",
                "Carne","Carnicería Ribera"),
            Product("carne_4","Pechuga de pollo","Magras y jugosas",6.0,15,
                "https://mejorconsalud.as.com/wp-content/uploads/2018/04/dos-pechugas-de-pollo.jpg?auto=webp&quality=7500&width=1920&crop=16:9,smart,safe&format=webp&optimize=medium&dpr=2&fit=cover&fm=webp&q=75&w=1920&h=1080",
                "Carne","Carnicería Ribera"),
            Product("carne_5","Chuletón","Maduración 30 días",64.5,3,
                "https://tienda.hostalrioara.com/wp-content/uploads/2020/06/chuleton-de-ternera.jpg",
                "Carne","Carnicería Ribera"),
        )

        val pescado = listOf(
            Product("pesc_1","Merluza","Del Cantábrico",11.5,8,
                "https://imag.bonviveur.com/merluza-a-la-plancha.jpg",
                "Pescado","Pescadería Norte"),
            Product("pesc_2","Bacalao","Tradicional",10.5,10,
                "https://www.lavanguardia.com/files/image_449_220/files/fp/uploads/2020/08/12/5f33f6624d0ca.r_d.2788-2025-916.jpeg",
                "Pescado","Pescadería Norte"),
            Product("pesc_3","Bonito","Temporada",12.0,6,
                "https://www.conxemar.com/wp-content/uploads/2022/12/sarda_chiliensis_el.jpeg",
                "Pescado","Pescadería Norte"),
            Product("pesc_4","Salmón","Noruego",15.8,5,
                "https://pescadosymariscos.consumer.es/sites/pescadosymariscos/files/styles/pagina_cabecera_desktop/public/2025-05/salmon_0.webp?h=6b0af028&itok=EtLAz0gR",
                "Pescado","Pescadería Norte"),
            Product("pesc_5","Sardinas","Frescas",11.6,20,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-2_1bEhTT5u2DkJa9pybOt1QXVL2IKAVftg&s",
                "Pescado","Pescadería Norte"),
        )

        val bebidas = listOf(
            Product("beb_1","Zumo de naranja","Natural exprimido",5.6,30,
                "https://www.dia.es/product_images/50690/50690_ISO_0_ES.jpg",
                "Bebidas","Frutería Sol"),
            Product("beb_2","Coca-Cola","Lata 330ml",2.2,100,
                "https://tucervezaadomicilio.com/wp-content/uploads/2020/07/lata-coca-cola.jpg",
                "Bebidas","Autoservicio Rio"),
            Product("beb_3","Red Bull","Lata 250ml",3.1,50,
                "https://www.dia.es/product_images/87367/87367_ISO_0_ES.jpg",
                "Bebidas","Autoservicio Rio"),
            Product("beb_4","Agua","Botella 1.5L",1.5,200,
                "https://m.media-amazon.com/images/I/61mTu-foNxL.jpg",
                "Bebidas","Autoservicio Rio"),
            Product("beb_5","Pepsi","Lata 330ml",2.1,80,
                "https://www.confisur.es/865-medium_default/pepsi-cola-lata-33cl.jpg",
                "Bebidas","Autoservicio Rio"),
        )

        val pasteles = listOf(
            Product("pas_1","Pastel Vasco","Relleno de crema",24.0,4,
                "https://labasedelapasteleria.com/wp-content/uploads/pastel-vasco-tradicional-como-hacer.jpg",
                "Pasteles","Pastelería Dulce"),
            Product("pas_2","Tarta Pepisandwich","Chocolate y crema",31.5,3,
                "https://www.pepinapastel.es/wp-content/uploads/2023/06/Tarta-sandwich-1-Cuad-scaled.jpg",
                "Pasteles","Pastelería Dulce"),
            Product("pas_3","Tarta de Queso","Tarta de queso",8.0,15,
                "https://i.blogs.es/6ad7a5/tarta-de-queso-philadelphia2/450_1000.jpg",
                "Pasteles","Pastelería Dulce"),
            Product("pas_4","Caja Pepiboms","Caja pepiboms",9.6,10,
                "https://www.pepinapastel.es/wp-content/uploads/2020/11/PB3-C-scaled.jpg",
                "Pasteles","Pastelería Dulce"),
            Product("pas_5","Tarta Explosión de chocolate","Triple choco",5.9,10, // <- ID único
                "https://albayzin2020.com/wp-content/uploads/2021/07/20-scaled.jpg",
                "Pasteles","Pastelería Dulce"),
        )

        return carne + pescado + bebidas + pasteles
    }
}
