package com.pedrobonini.oitavacerta.app.metronome

import androidx.lifecycle.ViewModel
import com.pedrobonini.oitavacerta.audioengine.metronome.MetronomeEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MetronomeViewModel @JvmOverloads constructor(
    private val engine: MetronomeEngine = MetronomeEngine(),
) : ViewModel() {

    val isPlaying: StateFlow<Boolean> = engine.isPlaying
    val currentBeat: StateFlow<Int> = engine.currentBeat

    private val _bpm = MutableStateFlow(120)
    val bpm: StateFlow<Int> = _bpm.asStateFlow()

    private val _beatsPerMeasure = MutableStateFlow(4)
    val beatsPerMeasure: StateFlow<Int> = _beatsPerMeasure.asStateFlow()

    fun setBpm(value: Int) {
        val clamped = value.coerceIn(MIN_BPM, MAX_BPM)
        _bpm.value = clamped
        engine.setBpm(clamped)
    }

    fun setBeatsPerMeasure(value: Int) {
        val clamped = value.coerceIn(1, 12)
        _beatsPerMeasure.value = clamped
        engine.setBeatsPerMeasure(clamped)
    }

    fun togglePlay() {
        if (isPlaying.value) {
            engine.stop()
        } else {
            engine.setBpm(bpm.value)
            engine.setBeatsPerMeasure(beatsPerMeasure.value)
            engine.start()
        }
    }

    override fun onCleared() {
        engine.stop()
    }

    private companion object {
        const val MIN_BPM = 30
        const val MAX_BPM = 300
    }
}
