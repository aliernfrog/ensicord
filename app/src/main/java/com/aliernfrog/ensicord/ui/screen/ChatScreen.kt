package com.aliernfrog.ensicord.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.model.ChatModel
import com.aliernfrog.ensicord.ui.screen.chatScreen.channelsPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.messagesPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.usersPanel
import com.aliernfrog.ensicord.ui.sheet.UserSheet
import com.aliernfrog.toptoast.TopToastManager
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private var recompose = mutableStateOf(true)
private lateinit var scope: CoroutineScope
private lateinit var userSheetUser: User
@OptIn(ExperimentalMaterialApi::class) private lateinit var userSheetState: ModalBottomSheetState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatScreen(chatModel: ChatModel, topToastManager: TopToastManager, navController: NavController) {
    val panelsState = rememberOverlappingPanelsState()
    scope = rememberCoroutineScope()
    userSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    OverlappingPanels(
        panelsState = panelsState,
        panelStart = channelsPanel(chatModel, panelsState, navController),
        panelEnd = usersPanel(chatModel, onUserSheetRequest = { showUserSheet(it) }),
        panelCenter = messagesPanel(chatModel, topToastManager, panelsState, onUserSheetRequest = { showUserSheet(it) })
    )
    UserSheet(
        user = if (::userSheetUser.isInitialized) userSheetUser else null,
        sheetState = userSheetState,
        onNameClick = { topToastManager.showToast(userSheetUser.name) }
    )
    recompose.value
}

@OptIn(ExperimentalMaterialApi::class)
private fun showUserSheet(user: User) {
    userSheetUser = user
    recompose.value = !recompose.value
    scope.launch { userSheetState.show() }
}