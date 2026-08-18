package com.pedrobonini.oitavacerta.uitheme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Cores de ESTADO do afinador (agulha, nota, Hz) — independentes do
 * ColorScheme padrão do Material3. O fundo nunca usa essas cores: reage
 * só ao tema claro/escuro.
 */
data class MatrixStateColors(
    val neutral: Color,
    val detecting: Color,
    val inTune: Color,
)

val LocalMatrixColors = staticCompositionLocalOf {
    MatrixStateColors(
        neutral = Color(0xFF8A8A8A),
        detecting = Color(0xFFFF1744),
        inTune = Color(0xFF00E676),
    )
}
