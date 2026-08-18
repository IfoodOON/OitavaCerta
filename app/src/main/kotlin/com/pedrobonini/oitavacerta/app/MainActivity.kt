package com.pedrobonini.oitavacerta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pedrobonini.oitavacerta.app.nav.OitavaCertaApp
import com.pedrobonini.oitavacerta.uitheme.OitavaCertaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            OitavaCertaTheme {
                OitavaCertaApp()
            }
        }
    }
}
