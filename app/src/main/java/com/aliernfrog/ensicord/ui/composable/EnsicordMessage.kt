package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.util.GeneralUtil

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnsicordMessage(message: Message, checkMention: String? = null, onAvatarClick: (() -> Unit)? = null, onNameClick: (() -> Unit)? = null, onLongClick: (()-> Unit)? = null) {
    val avatar = GeneralUtil.getAvatarPainter(message.author.avatar)
    var modifier = Modifier.combinedClickable(indication = rememberRipple(), interactionSource = remember { MutableInteractionSource() }, onLongClick = onLongClick, onClick = {})
    if (checkMention != null && message.content.contains("@$checkMention")) modifier = modifier.background(Color(0x2BFFF700))
    Row(modifier.padding(8.dp)) {
        Image(painter = avatar, contentDescription = "", Modifier.padding(end = 8.dp).clip(CircleShape).size(44.dp, 44.dp).clickable {
            if (onAvatarClick != null) onAvatarClick()
        })
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