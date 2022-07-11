package com.aliernfrog.ensicord.ui.screen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.data.UserStatus
import com.aliernfrog.ensicord.ui.screen.chatScreen.channelsPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.messagesPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.usersPanel
import com.aliernfrog.ensicord.utils.EnsiUtil
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState

class ChatModel(context: Context, config: SharedPreferences): ViewModel() {
    private val userStatus = config.getString("userStatus", null)
    val chosenChannel = "#ensicord-development"
    val userUser = User("user", config.getString("username", "Some frok")!!, "user", if (userStatus != null) UserStatus(userStatus) else null)
    val ensiUser = User("ensi", "Ensi", "ensi", EnsiUtil.getStatus(context), true)
    val users = listOf(userUser,ensiUser)
    val channels = listOf("#ensicord-development")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatScreen(navController: NavController, config: SharedPreferences) {
    val chatModel = ChatModel(LocalContext.current, config)
    val panelsState = rememberOverlappingPanelsState()
    OverlappingPanels(
        panelsState = panelsState,
        panelStart = channelsPanel(chatModel, navController),
        panelEnd = usersPanel(chatModel),
        panelCenter = messagesPanel(chatModel, panelsState)
    )
}