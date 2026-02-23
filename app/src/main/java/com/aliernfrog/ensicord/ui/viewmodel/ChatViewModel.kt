package com.aliernfrog.ensicord.ui.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensi.Ensi
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Channel
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.aliernfrog.toptoast.state.TopToastState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatViewModel(
    val prefs: PreferenceManager,
    val topToastState: TopToastState
) : ViewModel() {
    lateinit var uiScope: CoroutineScope

    val responseGenerator = Ensi(types = listOf(), schemes = listOf())
    val lazyListState = LazyListState()
    val drawerState = DrawerState(initialValue = DrawerValue.Closed)

    private val ensi = User(
        id = "ensi",
        name = "Ensi",
        avatarModel = R.drawable.ensi
    )
    var user by mutableStateOf(User(
        id = "user",
        name = prefs.userName.value,
        avatarModel = R.drawable.user
    ))

    val channels = mutableStateListOf(
        Channel("general"),
        Channel("offtopic"),
        Channel("news", readOnly = true),
        Channel("starboard", readOnly = true)
    )
    var chosenChannelIndex by mutableIntStateOf(0)
    var chosenChannel: Channel
        get() = channels[chosenChannelIndex]
        set(value) { chosenChannelIndex = channels.indexOfFirst { it == value } }

    var textInput: String
        get() = chosenChannel.chatInput.value
        set(value) { chosenChannel.chatInput.value = value }

    var lastMessageShownWithoutIME by mutableStateOf<Int?>(null)

    fun sendMessage(message: String, author: User, channel: Channel) {
        if (message.isBlank()) return
        channel.messages.add(Message(
            author = author,
            content = message
        ))
        if (channel.name == chosenChannel.name) uiScope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }

    fun sendMessageFromUserInput() {
        if (chosenChannel.readOnly) return
        val content = textInput
        val channel = chosenChannel
        sendMessage(
            message = content,
            author = user,
            channel = chosenChannel
        )
        textInput = ""
        sendEnsiMessage(input = content, channel = channel)
    }

    fun sendEnsiMessage(input: String, channel: Channel) {
        sendMessage(
            message = responseGenerator.generate(
                message = input
            ),
            author = ensi,
            channel = channel
        )
    }
}