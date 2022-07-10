package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.utils.GeneralUtil

@Composable
fun EnsicordMessage(message: Message, onNameClick: (() -> Unit)? = null) {
    val avatar = GeneralUtil.getAvatarId(message.author.avatar)
    Row(Modifier.clickable{}.padding(8.dp)) {
        Image(painter = painterResource(id = avatar), contentDescription = "", Modifier.padding(end = 8.dp).clip(CircleShape).size(44.dp, 44.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = message.author.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                    if (onNameClick != null) onNameClick()
                }
            )
            Text(text = message.content, color = MaterialTheme.colors.onBackground)
        }
    }
}