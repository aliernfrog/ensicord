package com.aliernfrog.ensicord

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aliernfrog.ensicord.model.AddonsModel
import com.aliernfrog.ensicord.model.ChatModel
import com.aliernfrog.ensicord.ui.screen.*
import com.aliernfrog.ensicord.ui.theme.EnsicordTheme
import com.aliernfrog.ensicord.util.EnsiUtil
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var config: SharedPreferences
    private lateinit var chatModel: ChatModel
    private lateinit var addonsModel: AddonsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EnsiUtil.prepare(
            _verbs = listOf("sobbed","adsed","feed","featured","faced","undefined","petted","mousing"),
            _words = listOf("me","you","we","they","alierns","indinibee","bees","momes","frogs","mouse","chicken","furries","frog","Exi's basement","free candies","ensi","van","laptop","marchmilos","mouse")
        )
        config = getSharedPreferences(ConfigKey.PREF_NAME, MODE_PRIVATE)
        chatModel = ChatModel(this, config)
        addonsModel = AddonsModel()
        checkDataDir()
        setContent {
            EnsicordTheme(getDarkThemePreference() ?: isSystemInDarkTheme()) {
                Box(Modifier.background(MaterialTheme.colors.background).fillMaxSize())
                SystemBars()
                Navigation()
            }
        }
    }

    @Composable
    private fun SystemBars() {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(MaterialTheme.colors.background)
    }

    @Composable
    private fun Navigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = NavDestinations.CHAT) {
            composable(route = NavDestinations.CHAT) {
                ChatScreen(chatModel, navController)
            }
            composable(route = NavDestinations.PROFILE) {
                ProfileScreen(chatModel, navController, config)
            }
            composable(route = NavDestinations.ADDONS) {
                AddonsScreen(navController, addonsModel, config)
            }
            composable(route = NavDestinations.ADDONS_REPOS) {
                AddonsReposScreen(navController, config)
            }
            composable(route = NavDestinations.OPTIONS) {
                OptionsScreen(navController, addonsModel, config)
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