package com.aliernfrog.ensicord.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.model.ChatModel
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

private var recompose = mutableStateOf(true)
private lateinit var scope: CoroutineScope
private var userSheetUser: User? = null
private var messageSheetMessage: Message? = null
@OptIn(ExperimentalMaterialApi::class) private lateinit var userSheetState: ModalBottomSheetState
@OptIn(ExperimentalMaterialApi::class) private lateinit var messageSheetState: ModalBottomSheetState

@OptIn(ExperimentalComposeUiApi::class) private var keyboardController: SoftwareKeyboardController? = null

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(chatModel: ChatModel, topToastManager: TopToastManager, navController: NavController) {
    val context = LocalContext.current
    val panelsState = rememberOverlappingPanelsState()
    keyboardController = LocalSoftwareKeyboardController.current
    scope = rememberCoroutineScope()
    userSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    messageSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    OverlappingPanels(
        panelsState = panelsState,
        panelStart = channelsPanel(chatModel, panelsState, navController),
        panelEnd = usersPanel(chatModel, onUserSheetRequest = { showUserSheet(it) }),
        panelCenter = messagesPanel(chatModel, topToastManager, panelsState, onUserSheetRequest = { showUserSheet(it) }, onMessageSheetRequest = { showMessageSheet(it) })
    )
    MessageSheet(
        message = messageSheetMessage,
        sheetState = messageSheetState,
        topToastManager = topToastManager,
        onUserSheetRequest = { showUserSheet(it) },
        onMessageDeleteRequest = { message ->
            chatModel.deleteMessage(message) { topToastManager.showToast(context.getString(R.string.chatDeletedMessage), iconDrawableId = R.drawable.trash, iconBackgroundColorType = TopToastColorType.PRIMARY) }
        }
    )
    UserSheet(
        user = userSheetUser,
        sheetState = userSheetState,
        onNameClick = { topToastManager.showToast(userSheetUser!!.name) }
    )
    LaunchedEffect(messageSheetState.currentValue) {
        if (messageSheetState.currentValue == ModalBottomSheetValue.Hidden) {
            messageSheetMessage = null
            recompose.value = !recompose.value
        }
    }
    LaunchedEffect(userSheetState.currentValue) {
        if (userSheetState.currentValue == ModalBottomSheetValue.Hidden) {
            userSheetUser = null
            recompose.value = !recompose.value
        }
    }
    recompose.value
}

@OptIn(ExperimentalMaterialApi::class)
private fun showUserSheet(user: User) {
    userSheetUser = user
    beforeSheetShow()
    scope.launch { userSheetState.show() }
}

@OptIn(ExperimentalMaterialApi::class)
private fun showMessageSheet(message: Message) {
    messageSheetMessage = message
    beforeSheetShow()
    scope.launch { messageSheetState.show() }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun beforeSheetShow() {
    recompose.value = !recompose.value
    keyboardController?.hide()
}