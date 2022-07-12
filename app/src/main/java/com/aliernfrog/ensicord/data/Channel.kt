package com.aliernfrog.ensicord.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Channel(
    val name: String,
    val messages: ArrayList<Message> = ArrayList(),
    val messageInput: MutableState<String> = mutableStateOf("")
)