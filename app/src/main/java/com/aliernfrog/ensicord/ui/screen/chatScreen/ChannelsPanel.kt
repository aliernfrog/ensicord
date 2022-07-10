package com.aliernfrog.ensicord.ui.screen.chatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.ui.composable.EnsicordChannel
import com.aliernfrog.ensicord.ui.composable.EnsicordUser
import com.aliernfrog.ensicord.ui.screen.ChatModel

@Composable
fun channelsPanel(chatModel: ChatModel): @Composable (BoxScope.() -> Unit) {
    return {
        Column(Modifier.clip(RoundedCornerShape(topEnd = 20.dp)).fillMaxSize().background(MaterialTheme.colors.secondary)) {
            LazyColumn(Modifier.weight(1f)) {
                items(chatModel.channels) { channel ->
                    EnsicordChannel(name = channel, chosen = chatModel.chosenChannel == channel)
                }
            }
            Column(Modifier.background(MaterialTheme.colors.secondary).clickable{}) {
                Box(modifier = Modifier.alpha(0.2f).fillMaxWidth().height(1.dp).background(MaterialTheme.colors.onBackground))
                EnsicordUser(user = chatModel.userUser)
            }
        }
    }
}