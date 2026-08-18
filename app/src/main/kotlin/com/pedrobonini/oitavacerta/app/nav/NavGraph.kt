package com.pedrobonini.oitavacerta.app.nav

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pedrobonini.oitavacerta.app.metronome.MetronomeScreen
import com.pedrobonini.oitavacerta.app.settings.SettingsScreen
import com.pedrobonini.oitavacerta.app.tuner.TunerScreen

@Composable
fun OitavaCertaApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Tuner.route,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
        ) {
            composable(Routes.Tuner.route) { TunerScreen() }
            composable(Routes.Metronome.route) { MetronomeScreen() }
            composable(Routes.Settings.route) { SettingsScreen() }
        }
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController) {
    Row(modifier = Modifier.fillMaxWidth()) {
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
