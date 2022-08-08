package com.aliernfrog.ensicord.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.aliernfrog.ensicord.model.ChatModel
import com.aliernfrog.ensicord.ui.screen.chatScreen.channelsPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.messagesPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.usersPanel
import com.aliernfrog.toptoast.TopToastManager
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatScreen(chatModel: ChatModel, topToastManager: TopToastManager, navController: NavController) {
    val panelsState = rememberOverlappingPanelsState()
    OverlappingPanels(
        panelsState = panelsState,
        panelStart = channelsPanel(chatModel, panelsState, navController),
        panelEnd = usersPanel(chatModel),
        panelCenter = messagesPanel(chatModel, topToastManager, panelsState)
    )
}