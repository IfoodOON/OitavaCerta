package com.pedrobonini.oitavacerta.tuningdata.catalog

import com.pedrobonini.oitavacerta.tuningdata.calc.PitchMath
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.ACOUSTIC_GUITAR
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.BASS
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.CAVACO
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.VIOLIN
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class TuningCatalogTest {

    private fun hzOfString(key: InstrumentKey, presetId: String, stringIndex: Int): Double {
        val preset = TuningCatalog.presetsFor(key).first { it.id == presetId }
        val string = preset.strings.first { it.stringIndex == stringIndex }
        return PitchMath.midiToHz(string.standardMidiNote.value)
    }

    private fun assertCloseTo(expected: Double, actual: Double, tolerance: Double = 0.05) {
        assertTrue(abs(expected - actual) <= tolerance, "esperado $expected, obtido $actual")
    }

    @Test
    fun `guitarra 6 cordas standard bate com a planilha de referencia`() {
        val key = InstrumentKey(ACOUSTIC_GUITAR, 6)
        assertCloseTo(329.63, hzOfString(key, "acoustic6_standard", 1))
        assertCloseTo(246.94, hzOfString(key, "acoustic6_standard", 2))
        assertCloseTo(196.00, hzOfString(key, "acoustic6_standard", 3))
        assertCloseTo(146.83, hzOfString(key, "acoustic6_standard", 4))
        assertCloseTo(110.00, hzOfString(key, "acoustic6_standard", 5))
        assertCloseTo(82.41, hzOfString(key, "acoustic6_standard", 6))
    }

    @Test
    fun `drop D abaixa somente a 6a corda em relacao ao standard`() {
        val key = InstrumentKey(ACOUSTIC_GUITAR, 6)
        assertCloseTo(146.83, hzOfString(key, "acoustic6_drop_d", 6))
        assertCloseTo(329.63, hzOfString(key, "acoustic6_drop_d", 1))
    }

    @Test
    fun `baixo 4 cordas standard bate com a planilha de referencia`() {
        val key = InstrumentKey(BASS, 4)
        assertCloseTo(98.00, hzOfString(key, "bass4_standard", 1))
        assertCloseTo(73.42, hzOfString(key, "bass4_standard", 2))
        assertCloseTo(55.00, hzOfString(key, "bass4_standard", 3))
        assertCloseTo(41.20, hzOfString(key, "bass4_standard", 4))
    }

    @Test
    fun `baixo 6 cordas cobre B0 a C3`() {
        val key = InstrumentKey(BASS, 6)
        assertCloseTo(130.81, hzOfString(key, "bass6_standard", 1))
        assertCloseTo(30.87, hzOfString(key, "bass6_standard", 6))
    }

    @Test
    fun `violino standard bate com 196-294-440-659`() {
        val key = InstrumentKey(VIOLIN, 4)
        assertCloseTo(659.25, hzOfString(key, "violin_standard", 1))
        assertCloseTo(440.00, hzOfString(key, "violin_standard", 2))
        assertCloseTo(293.66, hzOfString(key, "violin_standard", 3))
        assertCloseTo(196.00, hzOfString(key, "violin_standard", 4))
    }

    @Test
    fun `cavaco standard e D4-G4-B4-D5`() {
        val key = InstrumentKey(CAVACO, 4)
        assertCloseTo(587.33, hzOfString(key, "cavaco_standard", 1))
        assertCloseTo(493.88, hzOfString(key, "cavaco_standard", 2))
        assertCloseTo(392.00, hzOfString(key, "cavaco_standard", 3))
        assertCloseTo(293.66, hzOfString(key, "cavaco_standard", 4))
    }
}
