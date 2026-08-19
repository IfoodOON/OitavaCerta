package com.pedrobonini.oitavacerta.audioengine.metronome

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Process
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.PI
import kotlin.math.sin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Metrônomo com timing sample-accurate: o intervalo entre batidas é
 * calculado em número de amostras e escrito via AudioTrack em modo
 * STREAMING, nunca por Handler.postDelayed (que sofre drift). O clock
 * do hardware de áudio é a fonte de verdade.
 */
class MetronomeEngine(private val sampleRate: Int = 44100) {

    private var audioTrack: AudioTrack? = null
    private var playThread: Thread? = null
    private val running = AtomicBoolean(false)

    @Volatile private var bpm: Int = 120

    @Volatile private var beatsPerMeasure: Int = 4

    private val accentClick = renderClick(frequencyHz = 1500.0, durationMs = 30)
    private val normalClick = renderClick(frequencyHz = 1000.0, durationMs = 30)

    private val _currentBeat = MutableStateFlow(0)
    val currentBeat: StateFlow<Int> = _currentBeat.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    fun setBpm(newBpm: Int) {
        bpm = newBpm.coerceIn(MIN_BPM, MAX_BPM)
    }

    fun setBeatsPerMeasure(beats: Int) {
        beatsPerMeasure = beats.coerceIn(1, 12)
    }

    fun start() {
        if (running.getAndSet(true)) return
        _isPlaying.value = true

        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
            )
            .setBufferSizeInBytes(minBufferSize * 2)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
        audioTrack = track
        track.play()

        playThread = Thread({ runClickLoop(track) }, "OitavaCerta-Metronome").apply { start() }
    }

    fun stop() {
        if (!running.getAndSet(false)) return
        playThread?.join(200)
        playThread = null
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
        _isPlaying.value = false
        _currentBeat.value = 0
    }

    private fun runClickLoop(track: AudioTrack) {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        val silenceChunk = ShortArray(256)
        var beatIndex = 0
        var samplesWrittenInBeat = 0
        var samplesPerBeat = samplesPerBeatFor(bpm)

        while (running.get()) {
            samplesPerBeat = samplesPerBeatFor(bpm)

            if (samplesWrittenInBeat == 0) {
                val click = if (beatIndex % beatsPerMeasure == 0) accentClick else normalClick
                track.write(click, 0, click.size)
                _currentBeat.value = beatIndex % beatsPerMeasure
                samplesWrittenInBeat += click.size
            }

            val remaining = samplesPerBeat - samplesWrittenInBeat
            if (remaining > 0) {
                val chunkSize = minOf(remaining, silenceChunk.size)
                track.write(silenceChunk, 0, chunkSize)
                samplesWrittenInBeat += chunkSize
            } else {
                beatIndex++
                samplesWrittenInBeat = 0
            }
        }
    }

    private fun samplesPerBeatFor(currentBpm: Int) = (60.0 / currentBpm * sampleRate).toInt()

    private fun renderClick(frequencyHz: Double, durationMs: Int): ShortArray {
        val sampleCount = sampleRate * durationMs / 1000
        return ShortArray(sampleCount) { i ->
            val t = i.toDouble() / sampleRate
            val envelope = 1.0 - i.toDouble() / sampleCount
            val sample = sin(2.0 * PI * frequencyHz * t) * envelope
            (sample * Short.MAX_VALUE * 0.8).toInt().toShort()
        }
    }

    private companion object {
        const val MIN_BPM = 30
        const val MAX_BPM = 300
    }
}
