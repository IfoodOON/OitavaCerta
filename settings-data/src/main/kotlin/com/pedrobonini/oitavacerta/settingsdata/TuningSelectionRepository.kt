package com.pedrobonini.oitavacerta.settingsdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pedrobonini.oitavacerta.tuningdata.catalog.TuningCatalog
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.TuningPreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Mapa persistente (instrumento, nº cordas) -> afinação escolhida. Cada
 * combinação é uma chave independente: trocar de instrumento nunca
 * afeta a afinação lembrada de outro. Só muda quando o usuário mexe
 * manualmente naquela combinação específica.
 */
class TuningSelectionRepository(private val context: Context) {

    private val dataStore get() = context.oitavaCertaDataStore

    private fun keyFor(instrument: InstrumentKey) =
        stringPreferencesKey("tuning_selection_${instrument.type.name}_${instrument.stringCount}")

    fun observeSelectedTuning(instrument: InstrumentKey): Flow<TuningPreset> {
        val presets = TuningCatalog.presetsFor(instrument)
        return dataStore.data.map { prefs ->
            val savedId = prefs[keyFor(instrument)]
            presets.firstOrNull { it.id == savedId } ?: presets.first()
        }
    }

    suspend fun setSelectedTuning(instrument: InstrumentKey, preset: TuningPreset) {
        dataStore.edit { it[keyFor(instrument)] = preset.id }
    }
}
