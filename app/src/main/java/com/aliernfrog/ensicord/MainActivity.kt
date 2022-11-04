package com.aliernfrog.ensicord

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aliernfrog.ensicord.state.AddonsState
import com.aliernfrog.ensicord.state.ChatState
import com.aliernfrog.ensicord.ui.screen.*
import com.aliernfrog.ensicord.ui.theme.EnsicordTheme
import com.aliernfrog.ensicord.util.EnsiUtil
import com.aliernfrog.ensigeneration.EnsiConfig
import com.aliernfrog.toptoast.TopToastBase
import com.aliernfrog.toptoast.TopToastManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var config: SharedPreferences
    private lateinit var chatState: ChatState
    private lateinit var addonsState: AddonsState
    private lateinit var topToastManager: TopToastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        EnsiUtil.initialize(EnsiConfig())
        config = getSharedPreferences(ConfigKey.PREF_NAME, MODE_PRIVATE)
        chatState = ChatState(this, config)
        addonsState = AddonsState()
        topToastManager = TopToastManager()
        checkDataDir()
        setContent {
            val darkTheme = getDarkThemePreference() ?: isSystemInDarkTheme()
            EnsicordTheme(darkTheme) {
                TopToastBase(backgroundColor = MaterialTheme.colors.background, manager = topToastManager, content = { Navigation() })
                SystemBars(darkTheme)
            }
        }
    }

    @Composable
    private fun SystemBars(darkTheme: Boolean) {
        val controller = rememberSystemUiController()
        controller.statusBarDarkContentEnabled = !darkTheme
    }

    @Composable
    private fun Navigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = NavDestinations.CHAT) {
            composable(route = NavDestinations.CHAT) {
                ChatScreen(chatState, topToastManager, navController)
            }
            composable(route = NavDestinations.PROFILE) {
                ProfileScreen(chatState, topToastManager, navController, config)
            }
            composable(route = NavDestinations.ADDONS) {
                AddonsScreen(topToastManager, navController, addonsState, config)
            }
            composable(route = NavDestinations.ADDONS_REPOS) {
                AddonsReposScreen(navController, config)
            }
            composable(route = NavDestinations.OPTIONS) {
                OptionsScreen(topToastManager, navController, config)
            }
        }
    }

    private fun getDarkThemePreference(): Boolean? {
        val theme = config.getInt(ConfigKey.KEY_APP_THEME, Theme.SYSTEM)
        if (theme == Theme.LIGHT) return false
        if (theme == Theme.DARK) return true
        return null
    }

    private fun checkDataDir() {
        val file = File("${Environment.getExternalStorageDirectory()}${Path.PATH_DATA}")
        if (!file.isDirectory) file.mkdirs()
    }
}