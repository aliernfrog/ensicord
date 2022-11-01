package com.aliernfrog.ensicord.ui.screen.chatScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.state.ChatState
import com.aliernfrog.ensicord.ui.composable.EnsicordUser
import com.aliernfrog.ensicord.util.GeneralUtil

@Composable
fun usersPanel(chatState: ChatState, onUserSheetRequest: (User) -> Unit): @Composable (BoxScope.() -> Unit) {
    return {
        Column(Modifier.padding(top = GeneralUtil.getStatusBarHeight()).clip(RoundedCornerShape(topStart = 20.dp)).fillMaxSize().background(MaterialTheme.colors.secondary)) {
            LazyColumn {
                item {
                    Text(chatState.chosenChannel.name, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = MaterialTheme.colors.onBackground, modifier = Modifier.padding(16.dp))
                    Box(modifier = Modifier.alpha(0.2f).fillMaxWidth().height(1.dp).background(MaterialTheme.colors.onBackground))
                }
                items(chatState.users) { user ->
                    EnsicordUser(user, Modifier.clickable{ onUserSheetRequest(user) })
                }
                item {
                    Spacer(Modifier.animateContentSize().height(GeneralUtil.getNavigationBarHeight()))
                }
            }
        }
    }
}