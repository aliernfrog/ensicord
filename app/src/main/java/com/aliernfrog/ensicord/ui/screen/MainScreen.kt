package com.aliernfrog.ensicord.ui.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
            entry<Destination> { destination ->
                destination.content()
            }

            entry<SettingsDestination> { destination ->
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