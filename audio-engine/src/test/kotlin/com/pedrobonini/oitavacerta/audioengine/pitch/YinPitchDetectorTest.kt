package com.pedrobonini.oitavacerta.audioengine.pitch

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class YinPitchDetectorTest {

    private val sampleRate = 44100

    private fun sineWave(frequencyHz: Double, sampleCount: Int, amplitude: Float = 0.5f): FloatArray =
        FloatArray(sampleCount) { i ->
            (amplitude * sin(2.0 * PI * frequencyHz * i / sampleRate)).toFloat()
        }

    @Test
    fun `detecta 440Hz (A4) dentro de 1 por cento de tolerancia`() {
        val samples = sineWave(440.0, 4096)
        val result = YinPitchDetector.detectPitch(samples, sampleRate)
        assertNotNull(result)
        assertTrue(abs(result.hz - 440.0) < 4.4, "esperado ~440Hz, obtido ${result.hz}")
    }

    @Test
    fun `detecta 82_41Hz (E2, corda grave do violao) dentro de 2 por cento`() {
        val samples = sineWave(82.41, 4096)
        val result = YinPitchDetector.detectPitch(samples, sampleRate)
        assertNotNull(result)
        assertTrue(abs(result.hz - 82.41) < 1.65, "esperado ~82.41Hz, obtido ${result.hz}")
    }

    @Test
    fun `detecta 659_25Hz (E5, corda aguda do violino) dentro de 1 por cento`() {
        val samples = sineWave(659.25, 4096)
        val result = YinPitchDetector.detectPitch(samples, sampleRate)
        assertNotNull(result)
        assertTrue(abs(result.hz - 659.25) < 6.6, "esperado ~659.25Hz, obtido ${result.hz}")
    }

    @Test
    fun `silencio (amplitude zero) nao retorna pitch com confianca`() {
        val samples = FloatArray(4096)
        val result = YinPitchDetector.detectPitch(samples, sampleRate)
        assertNull(result)
    }

    @Test
    fun `janela pequena demais retorna null sem crashar`() {
        val samples = FloatArray(2)
        val result = YinPitchDetector.detectPitch(samples, sampleRate)
        assertNull(result)
    }
}
