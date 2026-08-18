package com.pedrobonini.oitavacerta.tuningdata.calc

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class PitchMathTest {

    private fun assertCloseTo(expected: Double, actual: Double, tolerance: Double = 0.01) {
        assertTrue(abs(expected - actual) <= tolerance, "esperado $expected, obtido $actual")
    }

    @Test
    fun `A4 com calibracao 440 retorna exatamente 440Hz`() {
        assertCloseTo(440.0, PitchMath.midiToHz(midiNote = 69, calibrationHz = 440.0))
    }

    @Test
    fun `A4 com calibracao 432 retorna exatamente 432Hz`() {
        assertCloseTo(432.0, PitchMath.midiToHz(midiNote = 69, calibrationHz = 432.0))
    }

    @Test
    fun `E2 standard bate com a planilha de referencia`() {
        assertCloseTo(82.41, PitchMath.midiToHz(midiNote = 40))
    }

    @Test
    fun `transposicao de menos 12 semitons e metade da frequencia`() {
        val base = PitchMath.midiToHz(midiNote = 69)
        val umaOitavaAbaixo = PitchMath.midiToHz(midiNote = 69, linearTranspositionSemitones = -12)
        assertCloseTo(base / 2.0, umaOitavaAbaixo)
    }

    @Test
    fun `hzToNearestNoteAndCents e o inverso de midiToHz para nota exata`() {
        val hz = PitchMath.midiToHz(midiNote = 45)
        val (nota, cents) = PitchMath.hzToNearestNoteAndCents(hz)
        assertTrue(nota == 45)
        assertCloseTo(0.0, cents, tolerance = 0.5)
    }
}
