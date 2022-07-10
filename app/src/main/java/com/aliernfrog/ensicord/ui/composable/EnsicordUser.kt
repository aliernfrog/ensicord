package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.utils.GeneralUtil

@Composable
fun EnsicordUser(user: User) {
    val avatar = GeneralUtil.getAvatarId(user.avatar)
    Row(Modifier.clickable{}.padding(8.dp)) {
        Image(painter = painterResource(id = avatar), contentDescription = "", Modifier.padding(end = 8.dp).clip(CircleShape).size(40.dp, 40.dp))
        Column(Modifier.weight(1f).align(CenterVertically)) {
            Text(text = user.name, color = MaterialTheme.colors.onBackground)
            if (user.status != null) Text(text = user.status, color = MaterialTheme.colors.onBackground, fontSize = 14.sp, modifier = Modifier.alpha(0.7f))
        }
    }
}