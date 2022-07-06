package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Message

@Composable
fun EnsicordMessage(message: Message) {
    val avatar = getAvatarId(message.avatar)
    Row(Modifier.padding(vertical = 8.dp)) {
        Image(painter = painterResource(id = avatar), contentDescription = "", Modifier.padding(end = 8.dp).clip(CircleShape).size(44.dp, 44.dp))
        Column(Modifier.weight(1f)) {
            Text(text = message.author, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground)
            Text(text = message.content, color = MaterialTheme.colors.onBackground)
        }
    }
}

private fun getAvatarId(avatar: String): Int {
    return when(avatar) {
        "ensi" -> R.drawable.ensi
        "system" -> R.drawable.system
        "user" -> R.drawable.user
        else -> R.drawable.user
    }
}