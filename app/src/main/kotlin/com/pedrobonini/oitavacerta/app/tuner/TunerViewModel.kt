package com.pedrobonini.oitavacerta.app.tuner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrobonini.oitavacerta.audioengine.pitch.PitchResult
import com.pedrobonini.oitavacerta.audioengine.tuner.TunerEngine
import com.pedrobonini.oitavacerta.tuningdata.calc.PitchMath
import com.pedrobonini.oitavacerta.tuningdata.model.MidiNote
import com.pedrobonini.oitavacerta.tuningdata.model.TuningPreset
import kotlin.math.abs
import kotlin.math.ln
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TunerViewModel @JvmOverloads constructor(
    private val tunerEngine: TunerEngine = TunerEngine(),
) : ViewModel() {

    private val targetTuning = MutableStateFlow<TuningPreset?>(null)

    val uiState: StateFlow<TunerUiState> = combine(tunerEngine.pitchResult, targetTuning) { pitch, tuning ->
        pitch?.let { toUiState(it, tuning) } ?: TunerUiState.idle()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TunerUiState.idle())

    fun updateTargetTuning(preset: TuningPreset?) {
        targetTuning.value = preset
    }

    fun onMicPermissionGranted() {
        tunerEngine.start(viewModelScope)
    }

    fun onScreenStopped() {
        tunerEngine.stop()
    }

    override fun onCleared() {
        tunerEngine.stop()
    }

    private fun toUiState(pitch: PitchResult, tuning: TuningPreset?): TunerUiState {
        val targetMidiNotes = tuning?.strings?.map { it.standardMidiNote.value }
        val (midiNote, cents) = if (targetMidiNotes.isNullOrEmpty()) {
            PitchMath.hzToNearestNoteAndCents(pitch.hz)
        } else {
            nearestTargetNoteAndCents(pitch.hz, targetMidiNotes)
        }
        val noteLabel = MidiNote(midiNote).pitchClass.label
        val status = if (abs(cents) <= IN_TUNE_TOLERANCE_CENTS) TunerStatus.IN_TUNE else TunerStatus.DETECTING
        return TunerUiState(status = status, noteLabel = noteLabel, hz = pitch.hz, cents = cents.toFloat())
    }

    /** Compara contra as cordas-alvo específicas da afinação atual, não a nota cromática genérica mais próxima. */
    private fun nearestTargetNoteAndCents(hz: Double, targetMidiNotes: List<Int>): Pair<Int, Double> =
        targetMidiNotes
            .map { midi -> midi to 1200.0 * ln(hz / PitchMath.midiToHz(midi)) / ln(2.0) }
            .minBy { abs(it.second) }

    private companion object {
        const val IN_TUNE_TOLERANCE_CENTS = 5.0
    }
}
