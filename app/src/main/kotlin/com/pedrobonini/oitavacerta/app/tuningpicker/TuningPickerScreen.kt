package com.pedrobonini.oitavacerta.app.tuningpicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pedrobonini.oitavacerta.tuningdata.catalog.TuningCatalog
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.TuningPreset
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

@Composable
fun TuningPickerScreen(
    instrument: InstrumentKey,
    selectedPresetId: String?,
    onSelect: (TuningPreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val neutral = LocalMatrixColors.current.neutral
    val presets = TuningCatalog.presetsFor(instrument)

    LazyColumn(modifier = modifier.fillMaxSize().padding(16.dp)) {
        items(presets) { preset ->
            val isSelected = preset.id == selectedPresetId
            Text(
                text = preset.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) LocalMatrixColors.current.inTune else neutral,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(preset) }
                    .padding(vertical = 16.dp),
            )
            HorizontalDivider(color = neutral.copy(alpha = 0.2f))
        }
    }
}
