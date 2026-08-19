package com.pedrobonini.oitavacerta.app.tuner

enum class TunerStatus { IDLE, DETECTING, IN_TUNE }

data class TunerUiState(
    val status: TunerStatus,
    val noteLabel: String,
    val hz: Double,
    val cents: Float,
    val instrumentLabel: String,
) {
    companion object {
        fun idle(instrumentLabel: String = "Guitarra") = TunerUiState(
            status = TunerStatus.IDLE,
            noteLabel = "--",
            hz = 0.0,
            cents = 0f,
            instrumentLabel = instrumentLabel,
        )
    }
}
