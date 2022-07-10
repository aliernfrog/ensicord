package com.aliernfrog.ensicord.ui.screen.chatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.ui.composable.EnsicordUser
import com.aliernfrog.ensicord.ui.screen.ChatModel

@Composable
fun usersPanel(chatModel: ChatModel): @Composable (BoxScope.() -> Unit) {
    return {
        Column(Modifier.clip(RoundedCornerShape(topStart = 20.dp)).fillMaxSize().background(MaterialTheme.colors.secondary)) {
            LazyColumn {
                item {
                    Text(chatModel.chatName, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = MaterialTheme.colors.onBackground, modifier = Modifier.padding(16.dp))
                    Box(modifier = Modifier.alpha(0.2f).fillMaxWidth().height(1.dp).background(MaterialTheme.colors.onBackground))
                }
                items(chatModel.users) { user ->
                    EnsicordUser(user)
                }
            }
        }
    }
}