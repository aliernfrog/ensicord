package com.aliernfrog.ensicord.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aliernfrog.ensicord.ui.screen.MainScreen
import com.aliernfrog.ensicord.ui.theme.EnsicordTheme
import com.aliernfrog.ensicord.ui.theme.Theme
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import com.aliernfrog.ensicord.util.sharedString
import com.aliernfrog.toptoast.component.TopToastHost
import io.github.aliernfrog.shared.ui.component.util.AppContainer
import io.github.aliernfrog.shared.ui.component.util.InsetsObserver
import io.github.aliernfrog.shared.util.LocalSharedString
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            AppContent()
        }
    }

    @Composable
    fun AppContent(
        mainViewModel: MainViewModel = koinViewModel()
    ) {
        val view = LocalView.current

        @Composable
        fun AppTheme(content: @Composable () -> Unit) {
            EnsicordTheme(
                darkTheme = mainViewModel.forceDarkTheme || shouldUseDarkTheme(mainViewModel.prefs.theme.value),
                dynamicColors = mainViewModel.prefs.materialYou.value,
                content = content
            )
        }

        AppTheme {
            InsetsObserver()
            CompositionLocalProvider(
                LocalSharedString provides sharedString
            ) {
                AppContainer {
                    MainScreen()
                    TopToastHost(mainViewModel.topToastState)
                }
            }
        }

        LaunchedEffect(Unit) {
            mainViewModel.topToastState.setComposeView(view)
            mainViewModel.topToastState.setAppTheme { AppTheme(it) }
        }
    }

    @Composable
    private fun shouldUseDarkTheme(theme: Int): Boolean {
        return when(theme) {
            Theme.LIGHT.int -> false
            Theme.DARK.int -> true
            else -> isSystemInDarkTheme()
        }
    }
}