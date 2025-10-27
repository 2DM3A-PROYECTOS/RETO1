# 🛒App MercadoRiberaGO — App Android para Mercado Local

[![Android](https://img.shields.io/badge/Android-Studio-green?logo=android)](https://developer.android.com/studio)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue?logo=kotlin)](https://kotlinlang.org/)
[![Firebase](https://img.shields.io/badge/Firebase-Backend-orange?logo=firebase)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](#-licencia)
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen)](https://github.com/2DM3A-PROYECTOS/RETO1/actions)


**App MercadoRiberaGo** es una aplicación móvil desarrollada en **Android Studio con Kotlin** que permite a los usuarios explorar los productos de un mercado local por categorías, crear una cuenta, añadir productos al carrito y realizar pedidos.  
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
📁 RETO1/
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── example/
│           │           └── reto1_dam_2025_26/
│           │               ├── ui/theme/       # Estilos y colores Compose
│           │               ├── data/           # Modelos y repositorios Firestore
│           │               ├── viewmodels/     # Lógica MVVM
│           │               └── utils/          # Funciones auxiliares
│           └── res/                            # Recursos (layouts, strings, etc.)
├── scripts/
│   ├── install_apk.sh        # Instalador automático de APK
│   └── send_email_lambda.js  # Envío de email desde AWS Lambda
├── gradle/

```
---

## 🧪 Instalación y ejecución

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/2DM3A-PROYECTOS/RETO1.git
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
(PRONTO)

---

### 🌟 Créditos
Proyecto desarrollado en el marco del módulo ** MercadoRiberaGo — 2º DAM (2025)**.  
Desarrolladores: Nikolai, Anthony, Lucas, Artem
