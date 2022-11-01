package com.aliernfrog.ensicord.ui.screen.chatScreen

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.ChatConstants
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.model.ChatModel
import com.aliernfrog.ensicord.ui.composable.EnsicordBorderlessButton
import com.aliernfrog.ensicord.ui.composable.EnsicordMessage
import com.aliernfrog.ensicord.ui.composable.EnsicordTextField
import com.aliernfrog.ensicord.util.EnsiUtil
import com.aliernfrog.ensicord.util.GeneralUtil
import com.aliernfrog.toptoast.TopToastColorType
import com.aliernfrog.toptoast.TopToastManager
import com.xinto.overlappingpanels.OverlappingPanelsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val recompose = mutableStateOf(true)
private lateinit var scope: CoroutineScope
private lateinit var topToastManager: TopToastManager
private lateinit var messageListState: LazyListState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun messagesPanel(chatModel: ChatModel, _topToastManager: TopToastManager, panelsState: OverlappingPanelsState, onUserSheetRequest: (User) -> Unit, onMessageSheetRequest: (Message) -> Unit): @Composable (BoxScope.() -> Unit) {
    scope = rememberCoroutineScope()
    messageListState = rememberLazyListState()
    topToastManager = _topToastManager
    return {
        Column(Modifier.background(MaterialTheme.colors.background)) {
            TopBar(chatModel, panelsState)
            ChatView(Modifier.fillMaxSize().weight(1f), chatModel, onUserSheetRequest, onMessageSheetRequest)
            ChatInput(chatModel)
            Spacer(Modifier.animateContentSize().height(5.dp+GeneralUtil.getNavigationBarHeight()))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TopBar(chatModel: ChatModel, panelsState: OverlappingPanelsState) {
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth().background(MaterialTheme.colors.secondary).padding(top = GeneralUtil.getStatusBarHeight()).padding(horizontal = 8.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        EnsicordBorderlessButton(painter = painterResource(id = R.drawable.menu), contentDescription = context.getString(R.string.chatChannels)) {
            scope.launch { panelsState.openStartPanel() }
        }
        Text(text = chatModel.chosenChannel.name, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 8.dp))
        EnsicordBorderlessButton(painter = painterResource(id = R.drawable.users), contentDescription = context.getString(R.string.chatUsers)) {
            scope.launch { panelsState.openEndPanel() }
        }
    }
}

@Composable
private fun ChatView(modifier: Modifier, chatModel: ChatModel, onUserSheetRequest: (User) -> Unit, onMessageSheetRequest: (Message) -> Unit) {
    val context = LocalContext.current
    Box(modifier, contentAlignment = Alignment.BottomEnd) {
        LazyColumn(verticalArrangement = Arrangement.Bottom, state = messageListState) {
            item {
                Text(
                    text = context.getString(R.string.chatBeginning).replace("%CHAT%", chatModel.chosenChannel.name),
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier.alpha(0.5f).padding(top = 100.dp, bottom = 60.dp, start = 8.dp, end = 8.dp)
                )
            }
            items(chatModel.chosenChannel.messages) { message ->
                EnsicordMessage(
                    message = message,
                    checkMention = chatModel.userUser.name,
                    onAvatarClick = { onUserSheetRequest(message.author) },
                    onNameClick = { chatModel.chosenChannel.messageInput.value += "@${message.author.name}" },
                    onLongClick = { onMessageSheetRequest(message) }
                )
            }
        }
        ScrollToBottom()
    }
    recompose.value
}

@Composable
private fun ScrollToBottom() {
    val context = LocalContext.current
    AnimatedVisibility(visible = !isAtBottom(), enter = slideInVertically() + fadeIn(), exit = slideOutVertically() + fadeOut()) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = context.getString(R.string.chatScrollToBottom),
            Modifier.padding(16.dp).size(40.dp).clip(CircleShape).background(Color.White).clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Black),
                onClick = { scrollToBottom(true) }
            )
        )
    }
}

@Composable
private fun ChatInput(chatModel: ChatModel) {
    val context = LocalContext.current
    val sendButtonEnabled = chatModel.chosenChannel.messageInput.value.trim() != "" && !chatModel.chosenChannel.readOnly
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.heightIn(0.dp, 160.dp)) {
        EnsicordTextField(
            value = chatModel.chosenChannel.messageInput.value,
            onValueChange = { chatModel.chosenChannel.messageInput.value = it },
            modifier = Modifier.weight(1f),
            enabled = !chatModel.chosenChannel.readOnly,
            placeholder = {
                if (chatModel.chosenChannel.readOnly) Text(text = context.getString(R.string.chatReadOnly))
                else Text(text = context.getString(R.string.chatSendMessage).replace("%CHAT%", chatModel.chosenChannel.name))
                          },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondaryVariant, textColor = MaterialTheme.colors.onSecondary, focusedIndicatorColor = MaterialTheme.colors.secondaryVariant, unfocusedIndicatorColor = MaterialTheme.colors.secondaryVariant)
        )
        AnimatedVisibility(visible = sendButtonEnabled) {
            Image(
                painter = painterResource(id = R.drawable.send),
                contentDescription = context.getString(R.string.chatSendMessageDescription),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp).size(height = 48.dp, width = 48.dp).clip(CircleShape).clickable {
                    if (sendButtonEnabled) {
                        if (chatModel.chosenChannel.messageInput.value.length <= ChatConstants.MESSAGE_CHAR_LIMIT) addMessage(Message(chatModel.getNextId(), chatModel.userUser, chatModel.chosenChannel.messageInput.value), chatModel, clearInput = true)
                        else topToastManager.showToast(context.getString(R.string.chatMessageTooLong).replace("%MAX%", ChatConstants.MESSAGE_CHAR_LIMIT.toString()), iconDrawableId = R.drawable.exclamation, iconTintColorType = TopToastColorType.ERROR)
                    }
                }
            )
        }
    }
}

private fun addMessage(message: Message, chatModel: ChatModel, clearInput: Boolean = false) {
    chatModel.sendMessage(message, clearInput) {
        recompose.value = !recompose.value
        scrollToBottom()
        if (message.author.id == "user") createEnsiResponse(message, chatModel)
    }
}

private fun createEnsiResponse(message: Message, chatModel: ChatModel) {
    val args = message.content.lowercase().split(" ")
    val response = if (args.contains("tell") && (args.contains("story") || args.contains("stories"))) EnsiUtil.getResponse(type = "LEGIT", sentenceCount = (5..50).random(), starting = true, punctuations = true)
    else EnsiUtil.getResponse(type = listOf("RAW","RAW","RAW","RAW","RAW","LEGIT","ALLCAPS").random(), lowCharChance = true)
    addMessage(Message(chatModel.getNextId(), chatModel.ensiUser, response), chatModel)
}

private fun scrollToBottom(force: Boolean = false) {
    if (isAtBottom() || force) scope.launch { messageListState.animateScrollToItem(messageListState.layoutInfo.totalItemsCount+1) }
}

private fun isAtBottom(): Boolean {
    val layoutInfo = messageListState.layoutInfo
    if (layoutInfo.visibleItemsInfo.lastOrNull() == null) return true
    return layoutInfo.visibleItemsInfo.last().index >= messageListState.layoutInfo.totalItemsCount - 7
}