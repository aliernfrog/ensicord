package com.aliernfrog.ensicord.enum

import androidx.compose.runtime.Composable
import com.aliernfrog.ensicord.ui.screen.ChatScreen
import com.aliernfrog.ensicord.ui.screen.ReelsScreen

enum class Destination(
    val content: @Composable () -> Unit
) {
    CHAT(
        content = {
            ChatScreen()
        }
    ),

    REELS(
        content = {
            ReelsScreen()
        }
    )
}