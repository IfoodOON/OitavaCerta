package com.pedrobonini.oitavacerta.tuningdata.catalog

import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.ACOUSTIC_GUITAR
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.BASS
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.CAVACO
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.ELECTRIC_GUITAR
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType.VIOLIN
import com.pedrobonini.oitavacerta.tuningdata.model.MidiNote
import com.pedrobonini.oitavacerta.tuningdata.model.TunedString
import com.pedrobonini.oitavacerta.tuningdata.model.TuningPreset

/**
 * Fonte única de todas as combinações instrumento+cordas -> afinações.
 * Cada preset é escrito como offset de semitons sobre a afinação standard
 * correspondente (documenta a origem musical), resolvido para MIDI absoluto
 * aqui em tempo de definição.
 *
 * TODO(produto): "UP C" (violão 7 e 8 cordas) ainda não tem as notas exatas
 * confirmadas pelo usuário — usa a afinação Standard como placeholder até
 * ser corrigido, para não inventar um valor musical errado silenciosamente.
 */
object TuningCatalog {

    // --- Guitarra/Violão 6 cordas standard (Lá4=440Hz), string1=aguda..string6=grave ---
    private val guitar6Standard = listOf(
        TunedString(1, MidiNote(64)), // E4  329.63
        TunedString(2, MidiNote(59)), // B3  246.94
        TunedString(3, MidiNote(55)), // G3  196.00
        TunedString(4, MidiNote(50)), // D3  146.83
        TunedString(5, MidiNote(45)), // A2  110.00
        TunedString(6, MidiNote(40)), // E2  82.41
    )
    private val guitar7Standard = guitar6Standard + TunedString(7, MidiNote(35)) // B1 61.74
    private val guitar8Standard = guitar7Standard + TunedString(8, MidiNote(30)) // F#1 46.25

    private val bass4Standard = listOf(
        TunedString(1, MidiNote(43)), // G2  98.00
        TunedString(2, MidiNote(38)), // D2  73.42
        TunedString(3, MidiNote(33)), // A1  55.00
        TunedString(4, MidiNote(28)), // E1  41.20
    )
    private val bass5Standard = bass4Standard + TunedString(5, MidiNote(23)) // B0 30.87
    private val bass6Standard = listOf(
        TunedString(1, MidiNote(48)), // C3  130.81
        TunedString(2, MidiNote(43)), // G2  98.00
        TunedString(3, MidiNote(38)), // D2  73.42
        TunedString(4, MidiNote(33)), // A1  55.00
        TunedString(5, MidiNote(28)), // E1  41.20
        TunedString(6, MidiNote(23)), // B0  30.87
    )

    private val violinStandard = listOf(
        TunedString(1, MidiNote(76)), // E5 659.25
        TunedString(2, MidiNote(69)), // A4 440.00
        TunedString(3, MidiNote(62)), // D4 293.66
        TunedString(4, MidiNote(55)), // G3 196.00
    )

    private val cavacoStandard = listOf(
        TunedString(1, MidiNote(74)), // D5 587.33
        TunedString(2, MidiNote(71)), // B4 493.88
        TunedString(3, MidiNote(67)), // G4 392.00
        TunedString(4, MidiNote(62)), // D4 293.66
    )

    private fun offsetPreset(
        id: String,
        displayName: String,
        base: List<TunedString>,
        offsets: Map<Int, Int> = emptyMap(),
    ) = TuningPreset(
        id = id,
        displayName = displayName,
        strings = base.map { s -> s.copy(standardMidiNote = s.standardMidiNote + (offsets[s.stringIndex] ?: 0)) },
    )

    private fun sixStringAlternateTunings(prefix: String, base: List<TunedString>) = listOf(
        offsetPreset("${prefix}_standard", "Standard", base),
        offsetPreset("${prefix}_drop_d", "DROP D", base, mapOf(6 to -2)),
        offsetPreset("${prefix}_dadgad", "DADGAD", base, mapOf(1 to -2, 2 to -2, 6 to -2)),
        offsetPreset("${prefix}_open_d", "OPEN D", base, mapOf(1 to -2, 2 to -2, 3 to -1, 6 to -2)),
        offsetPreset("${prefix}_open_g", "OPEN G", base, mapOf(1 to -2, 5 to -2, 6 to -2)),
        offsetPreset("${prefix}_open_c", "OPEN C", base, mapOf(2 to 1, 4 to -2, 5 to -2, 6 to -4)),
    )

    val catalog: Map<InstrumentKey, List<TuningPreset>> = buildMap {
        put(InstrumentKey(ACOUSTIC_GUITAR, 6), sixStringAlternateTunings("acoustic6", guitar6Standard))
        put(InstrumentKey(ELECTRIC_GUITAR, 6), sixStringAlternateTunings("electric6", guitar6Standard))

        put(
            InstrumentKey(ACOUSTIC_GUITAR, 7),
            listOf(
                offsetPreset("acoustic7_standard_b", "Standard B", guitar7Standard),
                offsetPreset("acoustic7_up_c", "UP C", guitar7Standard), // TODO(produto): notas exatas pendentes
            ),
        )
        put(
            InstrumentKey(ELECTRIC_GUITAR, 7),
            listOf(
                offsetPreset("electric7_standard", "Standard", guitar7Standard),
                offsetPreset("electric7_drop_a", "DROP A", guitar7Standard, mapOf(7 to -2)),
            ),
        )

        put(
            InstrumentKey(ACOUSTIC_GUITAR, 8),
            listOf(
                offsetPreset("acoustic8_standard_fsharp", "Standard F#", guitar8Standard),
                offsetPreset("acoustic8_up_c", "UP C", guitar8Standard), // TODO(produto): notas exatas pendentes
            ),
        )
        put(
            InstrumentKey(ELECTRIC_GUITAR, 8),
            listOf(
                offsetPreset("electric8_standard", "Standard", guitar8Standard),
                offsetPreset("electric8_drop_e", "DROP E", guitar8Standard, mapOf(8 to -2)),
            ),
        )

        put(
            InstrumentKey(BASS, 4),
            listOf(
                offsetPreset("bass4_standard", "Standard", bass4Standard),
                offsetPreset("bass4_drop_d", "Drop D", bass4Standard, mapOf(4 to -2)),
            ),
        )
        put(
            InstrumentKey(BASS, 5),
            listOf(
                offsetPreset("bass5_standard", "Standard", bass5Standard),
                offsetPreset("bass5_drop_a", "Drop A", bass5Standard, mapOf(5 to -2)),
            ),
        )
        put(InstrumentKey(BASS, 6), listOf(offsetPreset("bass6_standard", "Standard", bass6Standard)))

        put(
            InstrumentKey(VIOLIN, 4),
            listOf(
                offsetPreset("violin_standard", "Standard", violinStandard),
                offsetPreset("violin_aeae", "AEAE", violinStandard, mapOf(3 to 2, 4 to 2)),
                offsetPreset("violin_adae", "ADAE", violinStandard, mapOf(4 to 2)),
                offsetPreset("violin_gdgd", "GDGD", violinStandard, mapOf(1 to -2, 2 to -2)),
                offsetPreset("violin_gdad", "GDAD", violinStandard, mapOf(1 to -2)),
            ),
        )

        put(InstrumentKey(CAVACO, 4), listOf(offsetPreset("cavaco_standard", "Standard", cavacoStandard)))
    }

    fun presetsFor(key: InstrumentKey): List<TuningPreset> =
        catalog[key] ?: error("Nenhum preset de afinação cadastrado para $key")
}
