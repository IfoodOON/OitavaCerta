package com.pedrobonini.oitavacerta.app.tuner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

@Composable
fun InstrumentChip(
    instrumentLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val neutral = LocalMatrixColors.current.neutral
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, neutral),
    ) {
        Text(
            text = instrumentLabel.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = neutral,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
    }
}
