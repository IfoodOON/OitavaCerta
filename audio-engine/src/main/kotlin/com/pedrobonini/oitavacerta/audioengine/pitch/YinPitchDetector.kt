package com.pedrobonini.oitavacerta.audioengine.pitch

/**
 * Detecção de pitch via YIN (de Cheveigné & Kawahara). Evita erro de
 * oitava (comum em autocorrelação simples com cordas ricas em
 * harmônicos) usando a differença normalizada acumulada + limiar
 * absoluto, refinada por interpolação parabólica.
 *
 * Custo O(n^2/4) por chamada — aceitável para uma janela de poucos
 * milhares de amostras chamada poucas vezes por segundo (ver
 * TunerEngine), mas é o primeiro lugar a otimizar (ex. FFT) se um
 * dispositivo de entrada revelar lag perceptível na agulha.
 */
object YinPitchDetector {
    private const val ABSOLUTE_THRESHOLD = 0.15f

    fun detectPitch(samples: FloatArray, sampleRate: Int): PitchResult? {
        val maxTau = samples.size / 2
        if (maxTau < 2) return null

        val diff = differenceFunction(samples, maxTau)
        val cmnd = cumulativeMeanNormalizedDifference(diff, maxTau)
        val tauEstimate = absoluteThreshold(cmnd, maxTau) ?: return null

        val betterTau = parabolicInterpolation(cmnd, tauEstimate, maxTau)
        if (betterTau <= 0f) return null

        val hz = sampleRate / betterTau
        val confidence = (1f - cmnd[tauEstimate]).coerceIn(0f, 1f)
        return PitchResult(hz = hz.toDouble(), confidence = confidence.toDouble())
    }

    private fun differenceFunction(samples: FloatArray, maxTau: Int): FloatArray {
        val diff = FloatArray(maxTau)
        for (tau in 0 until maxTau) {
            var sum = 0f
            for (i in 0 until maxTau) {
                val delta = samples[i] - samples[i + tau]
                sum += delta * delta
            }
            diff[tau] = sum
        }
        return diff
    }

    private fun cumulativeMeanNormalizedDifference(diff: FloatArray, maxTau: Int): FloatArray {
        val cmnd = FloatArray(maxTau)
        cmnd[0] = 1f
        var runningSum = 0f
        for (tau in 1 until maxTau) {
            runningSum += diff[tau]
            cmnd[tau] = if (runningSum == 0f) 1f else diff[tau] * tau / runningSum
        }
        return cmnd
    }

    private fun absoluteThreshold(cmnd: FloatArray, maxTau: Int): Int? {
        var tau = 2
        while (tau < maxTau) {
            if (cmnd[tau] < ABSOLUTE_THRESHOLD) {
                while (tau + 1 < maxTau && cmnd[tau + 1] < cmnd[tau]) {
                    tau++
                }
                return tau
            }
            tau++
        }
        return null
    }

    private fun parabolicInterpolation(cmnd: FloatArray, tauEstimate: Int, maxTau: Int): Float {
        val x0 = if (tauEstimate < 1) tauEstimate else tauEstimate - 1
        val x2 = if (tauEstimate + 1 < maxTau) tauEstimate + 1 else tauEstimate
        if (x0 == tauEstimate) return if (cmnd[tauEstimate] <= cmnd[x2]) tauEstimate.toFloat() else x2.toFloat()
        if (x2 == tauEstimate) return if (cmnd[tauEstimate] <= cmnd[x0]) tauEstimate.toFloat() else x0.toFloat()

        val s0 = cmnd[x0]
        val s1 = cmnd[tauEstimate]
        val s2 = cmnd[x2]
        val denominator = 2 * (2 * s1 - s2 - s0)
        if (denominator == 0f) return tauEstimate.toFloat()
        return tauEstimate + (s2 - s0) / denominator
    }
}
