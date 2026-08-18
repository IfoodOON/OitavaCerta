package com.pedrobonini.oitavacerta.tuningdata.calc

import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Único ponto de cálculo de Hz do app. Calibração (Hz atribuído a A4) e
 * transposição linear (tons/semitons aplicados sobre toda a afinação) são
 * parâmetros ortogonais sempre resolvidos aqui.
 */
object PitchMath {
    const val REFERENCE_MIDI_A4 = 69

    fun midiToHz(
        midiNote: Int,
        calibrationHz: Double = 440.0,
        linearTranspositionSemitones: Int = 0,
    ): Double {
        val effectiveMidi = midiNote + linearTranspositionSemitones
        return calibrationHz * 2.0.pow((effectiveMidi - REFERENCE_MIDI_A4) / 12.0)
    }

    /** Retorna (nota MIDI mais próxima, desvio em cents) para um Hz detectado. */
    fun hzToNearestNoteAndCents(
        hz: Double,
        calibrationHz: Double = 440.0,
        linearTranspositionSemitones: Int = 0,
    ): Pair<Int, Double> {
        val rawMidi = REFERENCE_MIDI_A4 +
            12.0 * ln(hz / calibrationHz) / ln(2.0) - linearTranspositionSemitones
        val nearest = rawMidi.roundToInt()
        val cents = (rawMidi - nearest) * 100.0
        return nearest to cents
    }
}
