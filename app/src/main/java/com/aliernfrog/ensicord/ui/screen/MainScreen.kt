package com.aliernfrog.ensicord.ui.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aliernfrog.ensicord.enum.Destination
import com.aliernfrog.ensicord.ui.screen.settings.SettingsDestination
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import com.aliernfrog.ensicord.util.extension.removeLastIfMultiple
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel()
) {
    NavDisplay(
        backStack = mainViewModel.navigationBackStack,
        entryProvider = entryProvider {
            entry<Destination> { destination ->
                destination.content()
            }

            entry<SettingsDestination>(
                metadata = NavDisplay.transitionSpec {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start
                    ) + fadeIn() togetherWith slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start
                    ) + fadeOut()
                } + NavDisplay.popTransitionSpec {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End
                    ) togetherWith slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End
                    )
                } + NavDisplay.predictivePopTransitionSpec {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End
                    ) togetherWith slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) { destination ->
                destination.content(
                    /* onNavigateBackRequest = */ {
                        mainViewModel.navigationBackStack.removeLastIfMultiple()
                    },
                    /* onNavigateRequest = */ { target ->
                        mainViewModel.navigationBackStack.add(target)
                    }
                )
            }
        }
    )
}