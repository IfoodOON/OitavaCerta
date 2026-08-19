package com.pedrobonini.oitavacerta.app.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pedrobonini.oitavacerta.app.ads.AdBannerPlaceholder
import com.pedrobonini.oitavacerta.app.instrumentpicker.InstrumentPickerScreen
import com.pedrobonini.oitavacerta.app.metronome.MetronomeScreen
import com.pedrobonini.oitavacerta.app.settings.AppSettingsViewModel
import com.pedrobonini.oitavacerta.app.settings.SettingsScreen
import com.pedrobonini.oitavacerta.app.theme.ThemeToggleIcon
import com.pedrobonini.oitavacerta.app.tuner.TunerScreen
import com.pedrobonini.oitavacerta.app.tuningpicker.TuningPickerScreen

@Composable
fun OitavaCertaApp() {
    val navController = rememberNavController()
    val appSettingsViewModel: AppSettingsViewModel = viewModel()
    val isDarkTheme by appSettingsViewModel.isDarkTheme.collectAsState()
    val currentInstrument by appSettingsViewModel.currentInstrument.collectAsState()
    val currentTuning by appSettingsViewModel.currentTuning.collectAsState()

    Scaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ThemeToggleIcon(isLightTheme = !isDarkTheme, onToggle = appSettingsViewModel::toggleTheme)
                }
                AdBannerPlaceholder()
            }
        },
        bottomBar = {
            Column {
                AdBannerPlaceholder()
                AppBottomBar(navController)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Tuner.route,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
        ) {
            composable(Routes.Tuner.route) {
                TunerScreen(
                    instrument = currentInstrument,
                    tuning = currentTuning,
                    onInstrumentSelected = appSettingsViewModel::selectInstrument,
                )
            }
            composable(Routes.Metronome.route) { MetronomeScreen() }
            composable(Routes.Settings.route) {
                SettingsScreen(
                    instrumentLabel = currentInstrument,
                    tuningLabel = currentTuning?.displayName ?: "—",
                    onOpenInstrumentPicker = { navController.navigateSingleTop(Routes.InstrumentPicker.route) },
                    onOpenTuningPicker = { navController.navigateSingleTop(Routes.TuningPicker.route) },
                )
            }
            composable(Routes.InstrumentPicker.route) {
                InstrumentPickerScreen(
                    selected = currentInstrument,
                    onSelect = { key ->
                        appSettingsViewModel.selectInstrument(key)
                        navController.popBackStack()
                    },
                )
            }
            composable(Routes.TuningPicker.route) {
                TuningPickerScreen(
                    instrument = currentInstrument,
                    selectedPresetId = currentTuning?.id,
                    onSelect = { preset ->
                        appSettingsViewModel.selectTuning(preset)
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TextButton(onClick = { navController.navigateSingleTop(Routes.Metronome.route) }) {
            Text("Metrônomo")
        }
        TextButton(onClick = { /* TODO: share/rating intent */ }) {
            Text("Nos ajude")
        }
        TextButton(onClick = { navController.navigateSingleTop(Routes.Settings.route) }) {
            Text("Configurações")
        }
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) { saveState = true }
    }
}
