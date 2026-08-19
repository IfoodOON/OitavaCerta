package com.pedrobonini.oitavacerta.audioengine.tuner

import com.pedrobonini.oitavacerta.audioengine.capture.MicCaptureSource
import com.pedrobonini.oitavacerta.audioengine.pitch.PitchResult
import com.pedrobonini.oitavacerta.audioengine.pitch.YinPitchDetector
import kotlin.math.sqrt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Orquestra captura de mic + acúmulo em ring buffer + YIN + filtro de
 * silêncio/confiança, expondo o resultado mais recente via StateFlow.
 * A análise roda em intervalo fixo (não a cada chunk lido do mic), o
 * que desacopla a taxa de leitura do AudioRecord da taxa de análise.
 */
class TunerEngine(
    private val sampleRate: Int = 44100,
    private val windowSize: Int = 4096,
) {
    private val micCaptureSource = MicCaptureSource(sampleRate)
    private val ringBuffer = FloatArray(windowSize)
    private var writeIndex = 0
    private var filled = 0

    private val _pitchResult = MutableStateFlow<PitchResult?>(null)
    val pitchResult: StateFlow<PitchResult?> = _pitchResult.asStateFlow()

    private var engineJob: Job? = null

    fun start(scope: CoroutineScope) {
        if (engineJob?.isActive == true) return
        writeIndex = 0
        filled = 0

        engineJob = scope.launch(Dispatchers.Default) {
            launch {
                micCaptureSource.start().collect { chunk -> appendToRingBuffer(chunk) }
            }
            while (isActive) {
                delay(ANALYSIS_INTERVAL_MS)
                if (filled < windowSize) continue

                val window = ringBufferSnapshot()
                if (rms(window) < SILENCE_RMS_THRESHOLD) {
                    _pitchResult.value = null
                    continue
                }
                val result = YinPitchDetector.detectPitch(window, sampleRate)
                _pitchResult.value = result?.takeIf { it.confidence >= MIN_CONFIDENCE }
            }
        }
    }

    fun stop() {
        engineJob?.cancel()
        engineJob = null
        writeIndex = 0
        filled = 0
        _pitchResult.value = null
    }

    private fun appendToRingBuffer(chunk: FloatArray) {
        for (sample in chunk) {
            ringBuffer[writeIndex] = sample
            writeIndex = (writeIndex + 1) % windowSize
            if (filled < windowSize) filled++
        }
    }

    private fun ringBufferSnapshot(): FloatArray = FloatArray(windowSize) { i ->
        ringBuffer[(writeIndex + i) % windowSize]
    }

    private fun rms(samples: FloatArray): Float {
        var sum = 0f
        for (s in samples) sum += s * s
        return sqrt(sum / samples.size)
    }

    private companion object {
        const val ANALYSIS_INTERVAL_MS = 60L
        const val SILENCE_RMS_THRESHOLD = 0.01f
        const val MIN_CONFIDENCE = 0.85
    }
}
