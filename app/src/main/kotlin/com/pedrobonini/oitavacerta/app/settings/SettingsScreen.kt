package com.pedrobonini.oitavacerta.app.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pedrobonini.oitavacerta.app.instrumentpicker.displayName
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

@Composable
fun SettingsScreen(
    instrumentLabel: InstrumentKey,
    tuningLabel: String,
    onOpenInstrumentPicker: () -> Unit,
    onOpenTuningPicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        SettingsRow(
            title = "qual instrumento afinar",
            value = "${instrumentLabel.type.displayName().lowercase()} · ${instrumentLabel.stringCount} cordas",
            onClick = onOpenInstrumentPicker,
        )
        SettingsRow(
            title = "afinação do instrumento",
            value = tuningLabel,
            onClick = onOpenTuningPicker,
        )
        SettingsRow(title = "tonalidade linear", value = "padrão", onClick = {})
        SettingsRow(title = "calibrar", value = "440 hz", onClick = {})
        SettingsRow(title = "efeitos sonoros", value = "ativado", onClick = {})
        SettingsRow(title = "idioma", value = "português (brasil)", onClick = {})
        SettingsRow(title = "privacidade", value = "", onClick = {})
        SettingsRow(title = "versão do app", value = "0.1.0", onClick = {})
    }
}

@Composable
private fun SettingsRow(title: String, value: String, onClick: () -> Unit) {
    val neutral = LocalMatrixColors.current.neutral
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, color = neutral)
        if (value.isNotEmpty()) {
            Text(text = value, style = MaterialTheme.typography.labelSmall, color = neutral.copy(alpha = 0.7f))
        }
    }
    HorizontalDivider(color = neutral.copy(alpha = 0.2f))
}
