package com.aliernfrog.ensicord.ui.screen.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.rounded.Flare
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import com.aliernfrog.ensicord.util.extension.popBackStackSafe
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBackRequest: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navController = rememberNavController()
    val settingsNavController = remember { SettingsNavController(
        navigateBackFromSettings = onNavigateBackRequest,
        navigateBackToSettings = { navController.popBackStackSafe() },
        navigateInSettings = { navController.navigate(it.route) }
    ) }

    val destinations = remember { SettingsDestination.entries.toList() }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentDestination = destinations.find { it.route == currentRoute }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    stringResource(currentDestination?.title ?: R.string.settings)
                ) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = { currentDestination?.onNavigateBackRequest?.let {
                            it(settingsNavController)
                        } }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = SettingsDestination.ROOT.route,
            modifier = Modifier.padding(paddingValues).fillMaxWidth()
        ) {
            destinations.forEach { destination ->
                composable(destination.route) {
                    destination.content(settingsNavController)
                }
            }
        }
    }
}

@Composable
private fun SoonTMPage(
    mainViewModel: MainViewModel = koinViewModel(),
    onNavigateBackRequest: () -> Unit
) {
    LaunchedEffect(Unit) {
        mainViewModel.topToastState.showToast(
            text = "Soon™",
            icon = Icons.Rounded.Flare
        )
        onNavigateBackRequest()
    }
}

@Suppress("unused")
enum class SettingsDestination(
    val route: String,
    @StringRes val title: Int,
    @StringRes val description: Int?,
    val icon: ImageVector?,
    val showInPagesList: Boolean = true,
    val onNavigateBackRequest: (SettingsNavController) -> Unit = {
        it.navigateBackToSettings()
    },
    val content: @Composable (SettingsNavController) -> Unit
) {
    ROOT(
        route = "root",
        title = R.string.settings,
        description = null,
        icon = null,
        showInPagesList = false,
        onNavigateBackRequest = {
            it.navigateBackFromSettings()
        },
        content = { settingsNavController ->
            SettingsPage(
                onNavigateRequest = {
                    settingsNavController.navigateInSettings(it)
                }
            )
        }
    ),

    PROFILE(
        route = "profile",
        title = R.string.settings_profile,
        description = null,
        icon = null,
        showInPagesList = false,
        content = {
            SoonTMPage {
                it.navigateBackToSettings()
            }
        }
    ),

    APPEARANCE(
        route = "appearance",
        title = R.string.settings_appearance,
        description = R.string.settings_appearance_description,
        icon = Icons.Outlined.Palette,
        content = {
            AppearancePage()
        }
    ),

    ADDONS(
        route = "addons",
        title = R.string.settings_addons,
        description = R.string.settings_addons_description,
        icon = Icons.Outlined.Download,
        content = {
            SoonTMPage {
                it.navigateBackToSettings()
            }
        }
    ),

    BACKUP(
        route = "backup",
        title = R.string.settings_save,
        description = R.string.settings_save_description,
        icon = Icons.Outlined.Backup,
        content = {
            SoonTMPage {
                it.navigateBackToSettings()
            }
        }
    ),

    ABOUT(
        route = "about",
        title = R.string.settings_about,
        description = R.string.settings_about_description,
        icon = Icons.Outlined.Info,
        content = {
            SoonTMPage {
                it.navigateBackToSettings()
            }
        }
    )
}

data class SettingsNavController(
    val navigateBackToSettings: () -> Unit,
    val navigateBackFromSettings: () -> Unit,
    val navigateInSettings: (SettingsDestination) -> Unit
)