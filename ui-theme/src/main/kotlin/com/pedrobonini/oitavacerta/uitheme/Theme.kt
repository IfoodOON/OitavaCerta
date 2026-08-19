package com.pedrobonini.oitavacerta.uitheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val NeonRed = Color(0xFFFF1744)
private val NeonGreen = Color(0xFF00E676)
private val Neutral = Color(0xFF8A8A8A)

private val DarkColors = darkColorScheme(
    primary = Neutral,
    background = Color.Black,
    surface = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColors = lightColorScheme(
    primary = Neutral,
    background = Color.White,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun OitavaCertaTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val matrixColors = MatrixStateColors(neutral = Neutral, detecting = NeonRed, inTune = NeonGreen)
    CompositionLocalProvider(LocalMatrixColors provides matrixColors) {
        MaterialTheme(
            colorScheme = if (isDarkTheme) DarkColors else LightColors,
            typography = OitavaCertaTypography,
            content = content,
        )
    }
}
