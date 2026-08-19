package com.pedrobonini.oitavacerta.settingsdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Preferências gerais do app. Valores default aqui SEMPRE precisam
 * bater com os defaults hardcoded na UI (cold start não pode esperar
 * a primeira leitura assíncrona do disco).
 */
class AppSettingsRepository(private val context: Context) {

    private val dataStore get() = context.oitavaCertaDataStore

    val isDarkTheme: Flow<Boolean> = dataStore.data.map { it[AppPreferencesKeys.THEME_IS_DARK] ?: true }

    val languageTag: Flow<String> = dataStore.data.map { it[AppPreferencesKeys.LANGUAGE_TAG] ?: "pt-BR" }

    val calibrationHz: Flow<Double> = dataStore.data.map { it[AppPreferencesKeys.CALIBRATION_HZ] ?: 440.0 }

    val soundEffectsEnabled: Flow<Boolean> =
        dataStore.data.map { it[AppPreferencesKeys.SOUND_EFFECTS_ENABLED] ?: true }

    val linearTranspositionSemitones: Flow<Int> =
        dataStore.data.map { it[AppPreferencesKeys.LINEAR_TRANSPOSITION_SEMITONES] ?: 0 }

    val currentInstrument: Flow<InstrumentKey> = dataStore.data.map { prefs ->
        val type = prefs[AppPreferencesKeys.CURRENT_INSTRUMENT_TYPE]
            ?.let { runCatching { InstrumentType.valueOf(it) }.getOrNull() }
            ?: InstrumentType.ELECTRIC_GUITAR
        val stringCount = prefs[AppPreferencesKeys.CURRENT_STRING_COUNT] ?: type.defaultStringCount
        InstrumentKey(type, stringCount)
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { it[AppPreferencesKeys.THEME_IS_DARK] = isDark }
    }

    suspend fun setLanguageTag(tag: String) {
        dataStore.edit { it[AppPreferencesKeys.LANGUAGE_TAG] = tag }
    }

    suspend fun setCalibrationHz(hz: Double) {
        dataStore.edit { it[AppPreferencesKeys.CALIBRATION_HZ] = hz }
    }

    suspend fun setSoundEffectsEnabled(enabled: Boolean) {
        dataStore.edit { it[AppPreferencesKeys.SOUND_EFFECTS_ENABLED] = enabled }
    }

    suspend fun setLinearTranspositionSemitones(semitones: Int) {
        dataStore.edit { it[AppPreferencesKeys.LINEAR_TRANSPOSITION_SEMITONES] = semitones }
    }

    suspend fun setCurrentInstrument(key: InstrumentKey) {
        dataStore.edit {
            it[AppPreferencesKeys.CURRENT_INSTRUMENT_TYPE] = key.type.name
            it[AppPreferencesKeys.CURRENT_STRING_COUNT] = key.stringCount
        }
    }
}
