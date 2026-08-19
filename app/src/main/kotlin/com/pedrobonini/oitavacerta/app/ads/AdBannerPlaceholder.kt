package com.pedrobonini.oitavacerta.app.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Placeholder do espaço reservado para o banner adaptativo do AdMob.
 * Integração real do SDK entra na Fase 7 do plano — aqui só reserva o
 * espaço de layout pra não haver layout shift quando o anúncio chegar.
 */
@Composable
fun AdBannerPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF1A1A1A)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "AD", style = MaterialTheme.typography.labelSmall, color = Color(0xFF555555))
    }
}
