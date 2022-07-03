package com.aliernfrog.ensicord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aliernfrog.ensicord.ui.screen.ChatScreen
import com.aliernfrog.ensicord.ui.theme.EnsicordTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnsicordTheme() {
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
                ChatScreen()
            }
        }
    }
}