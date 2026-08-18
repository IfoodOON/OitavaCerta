package com.pedrobonini.oitavacerta.tuningdata.model

enum class PitchClass(val label: String) {
    C("C"), C_SHARP("C#"), D("D"), D_SHARP("D#"), E("E"), F("F"),
    F_SHARP("F#"), G("G"), G_SHARP("G#"), A("A"), A_SHARP("A#"), B("B"),
}

@JvmInline
value class MidiNote(val value: Int) {
    val pitchClass: PitchClass get() = PitchClass.entries[((value % 12) + 12) % 12]
    val octave: Int get() = (value / 12) - 1
    val label: String get() = "${pitchClass.label}$octave"

    operator fun plus(semitones: Int): MidiNote = MidiNote(value + semitones)
}
