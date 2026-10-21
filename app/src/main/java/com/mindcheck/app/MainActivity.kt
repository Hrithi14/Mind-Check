package com.mindcheck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mindcheck.app.core.designsystem.theme.MindCheckTheme
import com.mindcheck.app.data.local.datastore.ThemePreference
import com.mindcheck.app.presentation.navigation.MindCheckNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as? MindCheckApplication

        setContent {
            val themePreference by (app?.container?.userPreferencesRepository?.themePreference
                ?.collectAsState(initial = ThemePreference.SYSTEM)
                ?: remember { mutableStateOf(ThemePreference.SYSTEM) })

            val isDark = when (themePreference) {
                ThemePreference.SYSTEM -> isSystemInDarkTheme()
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
            }

            MindCheckTheme(darkTheme = isDark) {
                MindCheckNavHost()
            }
        }
    }
}
