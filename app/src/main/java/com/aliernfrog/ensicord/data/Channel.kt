package com.aliernfrog.ensicord.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Channel(
    val name: String,
    val messages: SnapshotStateList<Message> = SnapshotStateList(),
    val messageInput: MutableState<String> = mutableStateOf(""),
    val readOnly: Boolean = false
)