package com.aliernfrog.ensicord.ui.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aliernfrog.ensicord.enum.Destination
import com.aliernfrog.ensicord.ui.screen.settings.SettingsScreen
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import com.aliernfrog.ensicord.util.extension.removeLastIfMultiple
import com.aliernfrog.ensicord.util.slideTransitionMetadata
import io.github.aliernfrog.shared.ui.screen.settings.SettingsDestination
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    vm: MainViewModel = koinViewModel()
) {
    val onNavigateBackRequest: () -> Unit = {
        vm.navigationBackStack.removeLastIfMultiple()
    }

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
            entry<Destination> { destination ->
                destination.content()
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
}