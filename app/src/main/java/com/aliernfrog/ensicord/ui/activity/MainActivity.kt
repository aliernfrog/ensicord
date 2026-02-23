package com.aliernfrog.ensicord.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aliernfrog.ensicord.ui.screen.ChatScreen
import com.aliernfrog.ensicord.ui.screen.ReelsScreen
import com.aliernfrog.ensicord.ui.screen.settings.SettingsScreen
import com.aliernfrog.ensicord.ui.theme.EnsicordTheme
import com.aliernfrog.ensicord.ui.theme.Theme
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import com.aliernfrog.ensicord.util.Destination
import com.aliernfrog.ensicord.util.extension.removeLastIfMultiple
import com.aliernfrog.ensicord.util.sharedString
import com.aliernfrog.ensicord.util.slideTransitionMetadata
import com.aliernfrog.toptoast.component.TopToastHost
import io.github.aliernfrog.shared.ui.component.util.AppContainer
import io.github.aliernfrog.shared.ui.component.util.InsetsObserver
import io.github.aliernfrog.shared.ui.screen.settings.SettingsDestination
import io.github.aliernfrog.shared.util.LocalSharedString
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val vm = getViewModel<MainViewModel>()

        setContent {
            val view = LocalView.current

            @Composable
            fun AppTheme(content: @Composable () -> Unit) {
                EnsicordTheme(
                    darkTheme = vm.forceDarkTheme || shouldUseDarkTheme(vm.prefs.theme.value),
                    dynamicColors = vm.prefs.materialYou.value,
                    content = content
                )
            }

            AppTheme {
                CompositionLocalProvider(
                    LocalSharedString provides sharedString
                ) {
                    App(vm)
                }
            }

            LaunchedEffect(Unit) {
                vm.topToastState.setComposeView(view)
                vm.topToastState.setAppTheme { AppTheme(it) }
            }
        }
    }

    @Composable
    fun App(vm: MainViewModel) {
        val onNavigateBackRequest: () -> Unit = {
            vm.navigationBackStack.removeLastIfMultiple()
        }

        InsetsObserver()

        AppContainer {
            NavDisplay(
                backStack = vm.navigationBackStack,
                transitionSpec = {
                    ContentTransform(
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeIn(),
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeOut()
                    )
                },
                popTransitionSpec = {
                    ContentTransform(
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.End
                        ),
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End
                        )
                    )
                },
                predictivePopTransitionSpec = {
                    ContentTransform(
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.End
                        ),
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End
                        )
                    )
                },
                entryProvider = entryProvider {
                    entry<Destination.Chat> {
                        ChatScreen()
                    }

                    entry<Destination.Reels> {
                        ReelsScreen()
                    }

                    entry<SettingsDestination>(
                        metadata = slideTransitionMetadata
                    ) { destination ->
                        SettingsScreen(
                            destination = destination,
                            onNavigateBackRequest = onNavigateBackRequest,
                            onNavigateRequest = { vm.navigationBackStack.add(it) },
                            onCheckUpdatesRequest = {
                                // TODO vm.checkUpdates(skipVersionCheck = skipVersionCheck)
                            },
                            onNavigateUpdatesScreenRequest = {
                                // TODO vm.navigationBackStack.add(UpdateScreenDestination)
                            }
                        )
                    }
                }
            )

            TopToastHost(vm.topToastState)
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