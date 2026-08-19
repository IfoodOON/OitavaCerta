package com.pedrobonini.oitavacerta.app.instrumentpicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

@Composable
fun InstrumentSelectorContent(
    selected: InstrumentKey,
    onSelect: (InstrumentKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    val neutral = LocalMatrixColors.current.neutral
    LazyColumn(modifier = modifier) {
        items(InstrumentType.entries) { type ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = type.displayName(),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                    color = if (selected.type == type) neutral else neutral.copy(alpha = 0.6f),
                )
                if (type.availableStringCounts.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        type.availableStringCounts.forEach { count ->
                            val isSelected = selected.type == type && selected.stringCount == count
                            OutlinedButton(onClick = { onSelect(InstrumentKey(type, count)) }) {
                                Text(
                                    text = "$count cordas",
                                    color = if (isSelected) neutral else neutral.copy(alpha = 0.6f),
                                )
                            }
                        }
                    }
                } else {
                    TextButton(onClick = { onSelect(InstrumentKey(type, type.defaultStringCount)) }) {
                        Text(text = "Selecionar", color = neutral)
                    }
                }
            }
            HorizontalDivider(color = neutral.copy(alpha = 0.2f))
        }
    }
}
