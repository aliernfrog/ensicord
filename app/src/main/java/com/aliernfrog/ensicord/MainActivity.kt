package com.aliernfrog.ensicord

import android.content.SharedPreferences
import android.os.Bundle
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
import com.aliernfrog.ensicord.ui.screen.AddonsScreen
import com.aliernfrog.ensicord.ui.screen.ChatScreen
import com.aliernfrog.ensicord.ui.screen.OptionsScreen
import com.aliernfrog.ensicord.ui.screen.ProfileScreen
import com.aliernfrog.ensicord.ui.theme.EnsicordTheme
import com.aliernfrog.ensicord.util.EnsiUtil
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE)
        chatModel = ChatModel(this, config)
        addonsModel = AddonsModel()
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
        NavHost(navController = navController, startDestination = "chat") {
            composable(route = "chat") {
                ChatScreen(chatModel, navController)
            }
            composable(route = "profile") {
                ProfileScreen(chatModel, navController, config)
            }
            composable(route = "addons") {
                AddonsScreen(navController, addonsModel)
            }
            composable(route = "options") {
                OptionsScreen(navController, addonsModel, config)
            }
        }
    }

    private fun getDarkThemePreference(): Boolean? {
        val theme = config.getInt("appTheme", 0) //system
        if (theme == 1) return false //light
        if (theme == 2) return true //dark
        return null
    }
}