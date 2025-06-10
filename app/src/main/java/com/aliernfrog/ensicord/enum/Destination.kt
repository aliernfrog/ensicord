package com.aliernfrog.ensicord.enum

import androidx.compose.runtime.Composable
import com.aliernfrog.ensicord.ui.screen.ChatScreen

enum class Destination(
    val content: @Composable () -> Unit
) {
    CHAT(
        content = {
            ChatScreen()
        }
    )
}