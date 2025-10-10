# 🛒 MercadoGO — App Android para Mercado Local

**MercadoRiberaGo** es una aplicación móvil desarrollada en **Android Studio con Kotlin** que permite a los usuarios explorar los productos de un mercado local por categorías, crear una cuenta, añadir productos al carrito y realizar pedidos.  
Este proyecto fue creado como parte del **RETO 1 del ciclo DAM (Desarrollo de Aplicaciones Multiplataforma)**.

---

## 🚀 Características principales
- 🔐 **Autenticación de usuarios** con Firebase Auth (registro y login).
- 🛍️ **Catálogo de productos** organizado por categorías.
- 🧺 **Carrito de compras** con actualización en tiempo real.
- 📦 **Gestión de pedidos** (creación y estados: *Creado → Preparación → Listo*).
- ☁️ **Base de datos Firestore** para productos, usuarios, carritos y pedidos.
- 📸 **Almacenamiento de imágenes** con Firebase Storage.
- ✉️ **Notificación por email al iniciar sesión** mediante AWS Lambda + Gmail SMTP.
- ⚙️ **Arquitectura MVVM** y navegación con Jetpack Compose.
- 🔒 **Seguridad y análisis de rendimiento** (SINF + CYBERDEV).

---

## 🧱 Estructura del proyecto
```
📁 MercadoRiberaGO/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mercadogo/
│   │   │   │   ├── ui/        # Pantallas Compose (Inicio, Carrito, Pedido)
│   │   │   │   ├── data/      # Modelos y repositorios Firestore
│   │   │   │   ├── viewmodel/ # Lógica de negocio (MVVM)
│   │   │   │   └── utils/     # Funciones auxiliares
│   │   │   └── res/           # Recursos gráficos y layouts
│   ├── build.gradle
│   └── AndroidManifest.xml
├── scripts/
│   ├── install_apk.sh         # Script difusión (instalación APK)
│   └── send_email_lambda.js   # Script AWS Lambda (notificación email)
└── README.md
```
---

## 🧪 Instalación y ejecución

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/MercadoGO/App.git
cd App
```
---

## 👥 Equipo de desarrollo
| Rol | Integrante |
|-----|-------------|
| 🧩 Líder técnica / Arquitectura | **Anthony** |
| 🎨 Frontend / UI Compose | **Lucas** |
| 🧮 Base de datos / Auth Firebase | **Artem** |
| 🧰 QA / Seguridad / Rendimiento | **Nikolai** |

---

## 📸 Capturas de pantalla
> _(Pronto)_

---
### 🌟 Créditos
Proyecto desarrollado en el marco del módulo ** MercadoRiberaGo — 2º DAM (2025)**.  
Desarrolladores: Nikolai, Anthony, Lucas, Artem
