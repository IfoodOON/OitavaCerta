package com.pedrobonini.oitavacerta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pedrobonini.oitavacerta.app.nav.OitavaCertaApp
import com.pedrobonini.oitavacerta.app.settings.AppSettingsViewModel
import com.pedrobonini.oitavacerta.uitheme.OitavaCertaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val appSettingsViewModel: AppSettingsViewModel = viewModel()
            val isDarkTheme by appSettingsViewModel.isDarkTheme.collectAsState()

            OitavaCertaTheme(isDarkTheme = isDarkTheme) {
                OitavaCertaApp()
            }
        }
    }
}
