package com.aliernfrog.ensicord.ui.screen

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.state.ChatState
import com.aliernfrog.ensicord.ui.screen.chatScreen.channelsPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.messagesPanel
import com.aliernfrog.ensicord.ui.screen.chatScreen.usersPanel
import com.aliernfrog.ensicord.ui.sheet.MessageSheet
import com.aliernfrog.ensicord.ui.sheet.UserSheet
import com.aliernfrog.toptoast.TopToastColorType
import com.aliernfrog.toptoast.TopToastManager
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private lateinit var scope: CoroutineScope
private var userSheetUser: MutableState<User?> = mutableStateOf(null)
private var messageSheetMessage: MutableState<Message?> = mutableStateOf(null)
@OptIn(ExperimentalMaterialApi::class) private lateinit var userSheetState: ModalBottomSheetState
@OptIn(ExperimentalMaterialApi::class) private lateinit var messageSheetState: ModalBottomSheetState

@OptIn(ExperimentalComposeUiApi::class) private var keyboardController: SoftwareKeyboardController? = null

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(chatState: ChatState, topToastManager: TopToastManager, navController: NavController) {
    val context = LocalContext.current
    val panelsState = rememberOverlappingPanelsState()
    keyboardController = LocalSoftwareKeyboardController.current
    scope = rememberCoroutineScope()
    userSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    messageSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    OverlappingPanels(
        modifier = Modifier.imePadding(),
        panelsState = panelsState,
        panelStart = channelsPanel(chatState, panelsState, navController),
        panelEnd = usersPanel(chatState, onUserSheetRequest = { showUserSheet(it) }),
        panelCenter = messagesPanel(chatState, topToastManager, panelsState, onUserSheetRequest = { showUserSheet(it) }, onMessageSheetRequest = { showMessageSheet(it) })
    )
    MessageSheet(
        message = messageSheetMessage.value,
        sheetState = messageSheetState,
        topToastManager = topToastManager,
        onUserSheetRequest = { showUserSheet(it) },
        onMessageDeleteRequest = { message ->
            chatState.deleteMessage(message) { topToastManager.showToast(context.getString(R.string.chatDeletedMessage), iconDrawableId = R.drawable.trash, iconTintColorType = TopToastColorType.PRIMARY) }
        }
    )
    UserSheet(
        user = userSheetUser.value,
        sheetState = userSheetState,
        onNameClick = { topToastManager.showToast(userSheetUser.value?.name.toString()) }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
private fun showUserSheet(user: User) {
    userSheetUser.value = user
    keyboardController?.hide()
    scope.launch { userSheetState.show() }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
private fun showMessageSheet(message: Message) {
    messageSheetMessage.value = message
    keyboardController?.hide()
    scope.launch { messageSheetState.show() }
}