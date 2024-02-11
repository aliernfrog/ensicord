package com.aliernfrog.ensicord.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aliernfrog.ensicord.ui.component.InsetsObserver
import com.aliernfrog.ensicord.ui.screen.MainScreen
import com.aliernfrog.ensicord.ui.theme.EnsicordTheme
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import com.aliernfrog.toptoast.component.TopToastHost
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
            EnsicordTheme(content = content)
        }

        AppTheme {
            InsetsObserver()
            AppContainer {
                MainScreen()
                TopToastHost(mainViewModel.topToastState)
            }
        }

        LaunchedEffect(Unit) {
            mainViewModel.topToastState.setComposeView(view)
            mainViewModel.topToastState.setAppTheme { AppTheme(it) }
        }
    }

    @Composable
    private fun AppContainer(
        content: @Composable BoxScope.() -> Unit
    ) {
        val config = LocalConfiguration.current
        var modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
            modifier = modifier
                .displayCutoutPadding()
                .navigationBarsPadding()

        Box(
            modifier = modifier,
            content = content
        )
    }
}