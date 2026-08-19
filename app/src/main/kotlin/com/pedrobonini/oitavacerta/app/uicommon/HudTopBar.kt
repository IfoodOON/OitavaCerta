package com.pedrobonini.oitavacerta.app.uicommon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedrobonini.oitavacerta.app.theme.ThemeToggleIcon
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

/** Cabeçalho comum a todas as telas: lâmpada (tema) + wordmark + ícone de status. */
@Composable
fun HudTopBar(
    isLightTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val neutral = LocalMatrixColors.current.neutral
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ThemeToggleIcon(isLightTheme = isLightTheme, onToggle = onToggleTheme)
        Text(
            text = "OITAVACERTA",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp, letterSpacing = 4.sp),
            color = LocalMatrixColors.current.inTune,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        Icon(imageVector = Icons.Filled.Sensors, contentDescription = null, tint = neutral)
    }
}
