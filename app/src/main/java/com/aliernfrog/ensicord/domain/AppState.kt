package com.aliernfrog.ensicord.domain

import androidx.compose.runtime.mutableStateListOf
import com.aliernfrog.ensicord.util.Destination

class AppState {
    val navigationBackStack = mutableStateListOf<Any>(
        Destination.Chat
    )
}