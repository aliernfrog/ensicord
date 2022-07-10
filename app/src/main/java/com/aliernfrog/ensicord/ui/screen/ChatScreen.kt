package com.aliernfrog.ensicord.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.ui.screen.chatScreen.messagesPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.usersPanel
import com.aliernfrog.ensicord.utils.EnsiUtil
import com.xinto.overlappingpanels.OverlappingPanels

class ChatModel: ViewModel() {
    val chatName = "#ensicord-development"
    val userUser = User("user", "Some frok", "user")
    val ensiUser = User("ensi", "Ensi", "ensi", EnsiUtil.getResponse(), true)
    val users = listOf(userUser,ensiUser)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatScreen() {
    val chatModel = ChatModel()
    OverlappingPanels(
        panelStart = {},
        panelEnd = usersPanel(chatModel),
        panelCenter = messagesPanel(chatModel)
    )
}