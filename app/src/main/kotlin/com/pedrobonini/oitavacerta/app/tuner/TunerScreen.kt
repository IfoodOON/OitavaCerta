package com.pedrobonini.oitavacerta.app.tuner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

@Composable
fun TunerScreen(modifier: Modifier = Modifier) {
    val matrixColors = LocalMatrixColors.current
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "--",
            style = MaterialTheme.typography.displayLarge,
            color = matrixColors.neutral,
        )
        Text(
            text = "0.0 Hz",
            style = MaterialTheme.typography.bodyLarge,
            color = matrixColors.neutral,
        )
    }
}
