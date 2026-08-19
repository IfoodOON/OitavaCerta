package com.pedrobonini.oitavacerta.app.instrumentpicker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey

@Composable
fun InstrumentPickerScreen(
    selected: InstrumentKey,
    onSelect: (InstrumentKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    InstrumentSelectorContent(
        selected = selected,
        onSelect = onSelect,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    )
}
