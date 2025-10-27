package com.example.reto1_dam_2025_26.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Esquema CLARO: jerarquía limpia y contraste correcto
private val LightColorScheme = lightColorScheme(
    primary = Blanco,
    onPrimary = Blanco,
    primaryContainer = Blanco,
    onPrimaryContainer = NegroSuave,

    secondary = RojoMercado,
    onSecondary = NegroSuave,                 // texto oscuro sobre botón/acento claro
    secondaryContainer = VarianteSuperficieClara,
    onSecondaryContainer = NegroSuave,

    // Fondo y superficies
    background = FondoClaro,                  // pantalla: suave, no blanco “plano”
    onBackground = NegroSuave,
    surface = SuperficieClara,                // cards/containers
    onSurface = NegroSuave,
    surfaceVariant = VarianteSuperficieClara, // listas, divisores
    onSurfaceVariant = NegroSuave,

    // (opcional) tercer color para etiquetas/estadísticas
    tertiary = Tercario,
    onTertiary = OnTercario,

    // Estados
    error = Error,
    onError = OnError,

    outline = Outline
)

// Esquema OSCURO: mantiene la marca visible sin “empastar”
private val DarkColorScheme = darkColorScheme(
    primary = RojoMercado,
    onPrimary = Blanco,
    primaryContainer = VarianteSuperficieOscura,
    onPrimaryContainer = Blanco,

    secondary = Salmon,
    onSecondary = NegroSuave,                 // contraste alto sobre acento claro
    secondaryContainer = VarianteSuperficieOscura,
    onSecondaryContainer = Blanco,

    background = NegroSuave,                  // fondo principal oscuro
    onBackground = Blanco,
    surface = SuperficieOscura,               // cards/containers
    onSurface = Blanco,
    surfaceVariant = VarianteSuperficieOscura,
    onSurfaceVariant = Blanco,

    tertiary = Tercario,
    onTertiary = Blanco,

    error = Error,
    onError = OnError,

    outline = Outline
)


/**
 * Tema de la app.
 * - dynamicColor desactivado por defecto para respetar la identidad visual del mercado.
 * - Usa MaterialTheme.colorScheme.* y MaterialTheme.typography.* en tus pantallas.
 */
@Composable
fun Reto1_DAM_202526Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // ← mantenlo en false para conservar tu paleta
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
