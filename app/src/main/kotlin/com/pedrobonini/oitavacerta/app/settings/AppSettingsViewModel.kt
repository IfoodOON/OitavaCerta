package com.pedrobonini.oitavacerta.app.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pedrobonini.oitavacerta.settingsdata.AppSettingsRepository
import com.pedrobonini.oitavacerta.settingsdata.TuningSelectionRepository
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType
import com.pedrobonini.oitavacerta.tuningdata.model.TuningPreset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val DEFAULT_INSTRUMENT = InstrumentKey(InstrumentType.ELECTRIC_GUITAR, 6)

/**
 * Estado compartilhado entre telas (tema, instrumento atual, afinação
 * atual). Fonte de verdade = os repositórios de :settings-data,
 * expostos aqui como StateFlow para evitar leituras duplicadas de
 * DataStore por tela.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppSettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = AppSettingsRepository(application)
    private val tuningSelectionRepository = TuningSelectionRepository(application)

    val isDarkTheme: StateFlow<Boolean> =
        settingsRepository.isDarkTheme.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val currentInstrument: StateFlow<InstrumentKey> =
        settingsRepository.currentInstrument.stateIn(viewModelScope, SharingStarted.Eagerly, DEFAULT_INSTRUMENT)

    val currentTuning: StateFlow<TuningPreset?> = currentInstrument
        .flatMapLatest { tuningSelectionRepository.observeSelectedTuning(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun toggleTheme() {
        viewModelScope.launch { settingsRepository.setDarkTheme(!isDarkTheme.value) }
    }

    fun selectInstrument(key: InstrumentKey) {
        viewModelScope.launch { settingsRepository.setCurrentInstrument(key) }
    }

    fun selectTuning(preset: TuningPreset) {
        viewModelScope.launch { tuningSelectionRepository.setSelectedTuning(currentInstrument.value, preset) }
    }
}
