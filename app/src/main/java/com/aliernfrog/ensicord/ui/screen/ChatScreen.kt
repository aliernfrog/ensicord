package com.aliernfrog.ensicord.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.composable.EnsicordTextField

private val messageInput = mutableStateOf("")

private const val chatName = "#ensicord-development"

@Composable
fun ChatScreen() {
    Column {
        TopBar()
        ChatView(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).weight(1f))
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
    Column(modifier, verticalArrangement = Arrangement.Bottom) {
        Text(
            text = context.getString(R.string.chatBeginning).replace("%CHAT%", chatName),
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.alpha(0.5f).padding(top = 100.dp, bottom = 60.dp)
        )
        Text(text = "Ensi: hi bro\n\n\n\n\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\n===== END =====", color = MaterialTheme.colors.onBackground)
    }
}

@Composable
private fun ChatInput() {
    val context = LocalContext.current
    Row(Modifier.padding(end = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        EnsicordTextField(
            value = messageInput.value,
            onValueChange = { messageInput.value = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = context.getString(R.string.chatSendMessage).replace("%CHAT%", chatName)) },
            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = MaterialTheme.colors.secondaryVariant, unfocusedIndicatorColor = MaterialTheme.colors.secondaryVariant)
        )
        AnimatedVisibility(visible = messageInput.value.trim() != "") {
            IconButton(modifier = Modifier.height(48.dp).width(48.dp), onClick = { }) {
                Image(
                    painter = painterResource(id = R.drawable.send),
                    context.getString(R.string.chatSendMessageDescription)
                )
            }
        }
    }
}