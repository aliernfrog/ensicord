package com.aliernfrog.ensicord.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aliernfrog.ensicord.ui.screen.settings.SettingsScreen
import com.aliernfrog.ensicord.util.extension.popBackStackSafe

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MainDestination.CHAT
    ) {
        composable(MainDestination.CHAT) {
            ChatScreen(
                onNavigateSettingsRequest = {
                    navController.navigate(MainDestination.SETTINGS)
                }
            )
        }

        composable(MainDestination.SETTINGS) {
            SettingsScreen(
                onNavigateBackRequest = {
                    navController.popBackStackSafe()
                }
            )
        }
    }
}

object MainDestination {
    const val CHAT = "chat"
    const val SETTINGS = "settings"
}