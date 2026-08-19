package com.pedrobonini.oitavacerta.app.instrumentpicker

import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentType

// TODO(Fase 8): mover para strings.xml quando os idiomas forem externalizados.
fun InstrumentType.displayName(): String = when (this) {
    InstrumentType.ACOUSTIC_GUITAR -> "Violão"
    InstrumentType.ELECTRIC_GUITAR -> "Guitarra"
    InstrumentType.BASS -> "Baixo"
    InstrumentType.VIOLIN -> "Violino"
    InstrumentType.CAVACO -> "Cavaco"
}
