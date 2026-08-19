package com.pedrobonini.oitavacerta.app.uicommon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

/**
 * Botão minimalista estilo HUD: sem preenchimento, borda fina, cantos
 * retos — substitui o OutlinedButton padrão do Material3 (que usa
 * cores genéricas de outline e destoa da estética matrix). O texto é
 * usado exatamente como passado — controle de maiúsculas/minúsculas
 * fica a cargo de quem chama, seguindo o padrão de cada tela.
 */
@Composable
fun MatrixButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalMatrixColors.current.neutral,
    enabled: Boolean = true,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.5.sp),
        color = if (enabled) color else color.copy(alpha = 0.4f),
        textAlign = TextAlign.Center,
        modifier = modifier
            .border(BorderStroke(1.dp, color.copy(alpha = if (enabled) 0.7f else 0.3f)))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    )
}

/** Rótulo de texto simples sem borda, mesma tipografia — para itens de navegação/lista. */
@Composable
fun MatrixTextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalMatrixColors.current.neutral,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}
