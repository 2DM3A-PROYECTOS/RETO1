package com.example.reto1_dam_2025_26.ui.theme

import androidx.compose.ui.graphics.Color

// Marca
val RojoMercado = Color(0xFFB84332)   // color de acción principal (botones primarios)
val Salmon      = Color(0xFFF09D90)   // acento / acciones de apoyo

// Base neutra
val Blanco      = Color(0xFFFFFFFF)
val NegroSuave  = Color(0xFF131313)   // texto principal en claro / fondo en oscuro
val Gris        = Color(0xFF8A8686)

// Superficies (mejor lectura y jerarquía)
val FondoClaro            = Color(0xFFF9F6F5) // background (ligeramente cálido, menos “duro” que #FFF)
val SuperficieClara       = Blanco           // surface principal en modo claro
val VarianteSuperficieClara = Color(0xFFF0E7E5) // divisores, chips, cards sutiles

val SuperficieOscura        = Color(0xFF1F1B1A) // surface en modo oscuro
val VarianteSuperficieOscura = Color(0xFF2B2321) // surfaceVariant en oscuro

// Estados
val Error      = Color(0xFFBA1A1A)
val OnError    = Blanco

// Outline para bordes/divisores
val Outline    = Gris

// (opcional) Terceros/acento suave para tags o gráficos
val Tercario   = Color(0xFF7CB342)   // verde suave
val OnTercario = Color(0xFF0E2006)
