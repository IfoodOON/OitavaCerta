package com.pedrobonini.oitavacerta.app.instrumentpicker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey

/**
 * Overlay contextual da tela inicial — não é uma rota de navegação,
 * fecha ao selecionar um instrumento.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickInstrumentSheet(
    selected: InstrumentKey,
    onSelect: (InstrumentKey) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        InstrumentSelectorContent(
            selected = selected,
            onSelect = { key ->
                onSelect(key)
                onDismiss()
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}
