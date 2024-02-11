package com.aliernfrog.ensicord.ui.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
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
    private val user = User(
        id = "user",
        name = "Somemaus",
        avatarModel = R.drawable.user
    )

    var chosenChannel by mutableStateOf(Channel("general"))
    var textInput: String
        get() = chosenChannel.chatInput.value
        set(value) { chosenChannel.chatInput.value = value }

    var lastMessageShownWithoutIME by mutableStateOf<Int?>(null)

    fun sendMessageFromUserInput() {
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