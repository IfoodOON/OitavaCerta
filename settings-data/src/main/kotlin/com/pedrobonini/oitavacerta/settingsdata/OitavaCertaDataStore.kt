package com.pedrobonini.oitavacerta.settingsdata

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.oitavaCertaDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "oitava_certa_prefs",
)
