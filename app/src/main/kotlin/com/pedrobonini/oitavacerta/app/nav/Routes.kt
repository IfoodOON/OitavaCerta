package com.pedrobonini.oitavacerta.app.nav

sealed class Routes(val route: String) {
    data object Tuner : Routes("tuner")
    data object Metronome : Routes("metronome")
    data object Settings : Routes("settings")
    data object InstrumentPicker : Routes("instrument_picker")
    data object TuningPicker : Routes("tuning_picker")
}
