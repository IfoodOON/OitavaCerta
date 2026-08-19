package com.pedrobonini.oitavacerta.app.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pedrobonini.oitavacerta.app.instrumentpicker.InstrumentPickerScreen
import com.pedrobonini.oitavacerta.app.metronome.MetronomeScreen
import com.pedrobonini.oitavacerta.app.settings.AppSettingsViewModel
import com.pedrobonini.oitavacerta.app.settings.SettingsScreen
import com.pedrobonini.oitavacerta.app.tuner.TunerScreen
import com.pedrobonini.oitavacerta.app.tuningpicker.TuningPickerScreen
import com.pedrobonini.oitavacerta.app.uicommon.AdPlaceholder
import com.pedrobonini.oitavacerta.app.uicommon.HudTopBar
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

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
                HudTopBar(isLightTheme = !isDarkTheme, onToggleTheme = appSettingsViewModel::toggleTheme)
                AdPlaceholder(
                    label = "SYS_AD_PLACEHOLDER_01",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
        },
        bottomBar = {
            Column {
                AdPlaceholder(
                    label = "SYS_AD_PLACEHOLDER_02",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
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
    val neutral = LocalMatrixColors.current.neutral
    val active = LocalMatrixColors.current.inTune
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    fun colorFor(route: String): Color = if (currentRoute == route) active else neutral.copy(alpha = 0.6f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        BottomBarItem(
            icon = Icons.Filled.Tune,
            label = "Afinador",
            color = colorFor(Routes.Tuner.route),
            onClick = { navController.navigateSingleTop(Routes.Tuner.route) },
        )
        BottomBarItem(
            icon = Icons.Filled.Speed,
            label = "Metrônomo",
            color = colorFor(Routes.Metronome.route),
            onClick = { navController.navigateSingleTop(Routes.Metronome.route) },
        )
        BottomBarItem(
            icon = Icons.Filled.Favorite,
            label = "Nos ajude",
            color = neutral.copy(alpha = 0.6f),
            onClick = { /* TODO: share/rating intent */ },
        )
        BottomBarItem(
            icon = Icons.Filled.Settings,
            label = "Configurações",
            color = colorFor(Routes.Settings.route),
            onClick = { navController.navigateSingleTop(Routes.Settings.route) },
        )
    }
}

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 4.dp),
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) { saveState = true }
    }
}
