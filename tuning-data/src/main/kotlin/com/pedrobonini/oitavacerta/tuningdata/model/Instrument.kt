package com.pedrobonini.oitavacerta.tuningdata.model

enum class InstrumentType(val availableStringCounts: List<Int>, val defaultStringCount: Int) {
    ACOUSTIC_GUITAR(listOf(6, 7, 8), 6),
    ELECTRIC_GUITAR(listOf(6, 7, 8), 6),
    BASS(listOf(4, 5, 6), 4),
    VIOLIN(listOf(4), 4),
    CAVACO(listOf(4), 4),
}

data class InstrumentKey(val type: InstrumentType, val stringCount: Int)

/** stringIndex 1 = corda mais aguda, convenção tradicional. */
data class TunedString(val stringIndex: Int, val standardMidiNote: MidiNote)

data class TuningPreset(
    val id: String,
    val displayName: String,
    val strings: List<TunedString>,
)
