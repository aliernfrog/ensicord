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
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Channel
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.toptoast.state.TopToastState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatViewModel(
    val topToastState: TopToastState
) : ViewModel() {
    lateinit var uiScope: CoroutineScope

    val lazyListState = LazyListState()
    val drawerState = DrawerState(initialValue = DrawerValue.Closed)
    val user = User(
        id = "user",
        name = "Somemaus",
        avatarModel = R.drawable.user
    )

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

    fun sendMessageFromUserInput() {
        if (chosenChannel.readOnly) return
        if (textInput.isBlank()) return
        chosenChannel.messages.add(Message(
            author = user,
            content = textInput
        ))
        textInput = ""
        uiScope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }
}