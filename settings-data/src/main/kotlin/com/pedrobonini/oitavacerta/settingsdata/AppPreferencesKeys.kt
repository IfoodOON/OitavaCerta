package com.pedrobonini.oitavacerta.settingsdata

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object AppPreferencesKeys {
    val THEME_IS_DARK = booleanPreferencesKey("theme_is_dark")
    val LANGUAGE_TAG = stringPreferencesKey("language_tag")
    val CALIBRATION_HZ = doublePreferencesKey("calibration_hz")
    val SOUND_EFFECTS_ENABLED = booleanPreferencesKey("sound_fx_enabled")
    val CURRENT_INSTRUMENT_TYPE = stringPreferencesKey("current_instrument_type")
    val CURRENT_STRING_COUNT = intPreferencesKey("current_string_count")
    val LINEAR_TRANSPOSITION_SEMITONES = intPreferencesKey("linear_transposition_semitones")
}
