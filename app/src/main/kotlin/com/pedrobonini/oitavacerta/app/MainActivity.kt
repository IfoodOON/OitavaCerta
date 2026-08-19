package com.pedrobonini.oitavacerta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pedrobonini.oitavacerta.app.nav.OitavaCertaApp
import com.pedrobonini.oitavacerta.uitheme.OitavaCertaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            // TODO(Fase 6): substituir por preferencia persistida em :settings-data.
            var isDarkTheme by rememberSaveable { mutableStateOf(true) }
            OitavaCertaTheme(isDarkTheme = isDarkTheme) {
                OitavaCertaApp(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                )
            }
        }
    }
}
