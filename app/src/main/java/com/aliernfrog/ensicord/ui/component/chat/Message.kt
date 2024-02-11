package com.aliernfrog.ensicord.ui.component.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aliernfrog.ensicord.data.Message

@Composable
fun Message(
    message: Message,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AsyncImage(
            model = message.author.avatarModel,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(2.dp)
                .clip(CircleShape)
        )
        Column {
            Text(
                text = message.author.name,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}