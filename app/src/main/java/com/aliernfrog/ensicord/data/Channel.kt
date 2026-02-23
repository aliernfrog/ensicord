package com.aliernfrog.ensicord.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Channel(
    val name: String,
    val readOnly: Boolean = false,
    val chatInput: MutableState<String> = mutableStateOf(""),
    val messages: SnapshotStateList<Message> = mutableStateListOf()
)