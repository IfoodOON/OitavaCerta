package com.pedrobonini.oitavacerta.app.tuner

enum class TunerStatus { IDLE, DETECTING, IN_TUNE }

data class TunerUiState(
    val status: TunerStatus,
    val noteLabel: String,
    val octave: Int?,
    val hz: Double,
    val cents: Float,
) {
    companion object {
        fun idle() = TunerUiState(status = TunerStatus.IDLE, noteLabel = "--", octave = null, hz = 0.0, cents = 0f)
    }
}
