package com.aliernfrog.ensicord.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.ui.composable.EnsicordMessage
import com.aliernfrog.ensicord.ui.composable.EnsicordTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val messageInput = mutableStateOf("")
private val messageList = ArrayList<Message>()
private val recompose = mutableStateOf(true)

private const val chatName = "#ensicord-development"

private lateinit var scope: CoroutineScope
private lateinit var messageListState: LazyListState

@Composable
fun ChatScreen() {
    scope = rememberCoroutineScope()
    messageListState = rememberLazyListState()
    Column {
        TopBar()
        ChatView(Modifier.fillMaxSize().weight(1f))
        ChatInput()
        Spacer(Modifier.height(5.dp))
    }
}

@Composable
private fun TopBar() {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colors.secondary).padding(horizontal = 24.dp, vertical = 15.dp)) {
        Text(text = chatName, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
private fun ChatView(modifier: Modifier) {
    val context = LocalContext.current
    LazyColumn(modifier, verticalArrangement = Arrangement.Bottom, contentPadding = PaddingValues(horizontal = 8.dp), state = messageListState) {
        item {
            Text(
                text = context.getString(R.string.chatBeginning).replace("%CHAT%", chatName),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.alpha(0.5f).padding(top = 100.dp, bottom = 60.dp)
            )
        }
        items(messageList) { message ->
            EnsicordMessage(message)
        }
    }
    recompose.value
}

@Composable
private fun ChatInput() {
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.heightIn(0.dp, 160.dp)) {
        EnsicordTextField(
            value = messageInput.value,
            onValueChange = { messageInput.value = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = context.getString(R.string.chatSendMessage).replace("%CHAT%", chatName)) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondaryVariant, textColor = MaterialTheme.colors.onSecondary, focusedIndicatorColor = MaterialTheme.colors.secondaryVariant, unfocusedIndicatorColor = MaterialTheme.colors.secondaryVariant)
        )
        AnimatedVisibility(visible = messageInput.value.trim() != "") {
            IconButton(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp).height(48.dp).width(48.dp), enabled = messageInput.value.trim() != "", onClick = {
                addMessage(Message("user", "Some frok", messageInput.value), clearInput = true)
            }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.send),
                    context.getString(R.string.chatSendMessageDescription)
                )
            }
        }
    }
}

private fun addMessage(message: Message, clearInput: Boolean = false) {
    messageList.add(message)
    recompose.value = !recompose.value
    if (isAtBottom()) scope.launch { messageListState.animateScrollToItem(messageList.size) }
    if (clearInput) messageInput.value = ""
}

private fun isAtBottom(): Boolean {
    val layoutInfo = messageListState.layoutInfo
    if (layoutInfo.visibleItemsInfo.lastOrNull() == null) return true
    return layoutInfo.visibleItemsInfo.last().index >= messageListState.layoutInfo.totalItemsCount - 7
}