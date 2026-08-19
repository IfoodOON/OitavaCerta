package com.pedrobonini.oitavacerta.audioengine.capture

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Captura contínua do microfone via AudioRecord, emitindo blocos de
 * amostras normalizadas em [-1, 1]. Chamador precisa garantir que a
 * permissão RECORD_AUDIO já foi concedida antes de coletar o Flow —
 * caso contrário o AudioRecord lança SecurityException.
 */
class MicCaptureSource(private val sampleRate: Int = 44100) {

    @SuppressLint("MissingPermission") // caller garante RECORD_AUDIO antes de chamar start()
    fun start(): Flow<FloatArray> = callbackFlow {
        val minBufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )
        if (minBufferSize <= 0) {
            close(IllegalStateException("Dispositivo não suporta esta configuração de áudio"))
            return@callbackFlow
        }
        val bufferSize = maxOf(minBufferSize, 2048)

        val record = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
        )

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            record.release()
            close(IllegalStateException("Falha ao inicializar AudioRecord"))
            return@callbackFlow
        }

        val running = AtomicBoolean(true)
        record.startRecording()

        val captureThread = Thread({
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            val shortBuffer = ShortArray(bufferSize / 2)
            while (running.get()) {
                val read = record.read(shortBuffer, 0, shortBuffer.size)
                if (read > 0) {
                    val normalized = FloatArray(read) { i -> shortBuffer[i] / 32768f }
                    trySend(normalized)
                }
            }
        }, "OitavaCerta-MicCapture")
        captureThread.start()

        awaitClose {
            running.set(false)
            record.stop()
            record.release()
        }
    }
}
