package com.aliernfrog.ensicord.ui.composable

import android.os.Environment
import androidx.compose.foundation.Image
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
import coil.compose.rememberAsyncImagePainter
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.util.GeneralUtil

@Composable
fun EnsicordUser(user: User, modifier: Modifier = Modifier) {
    val avatar = GeneralUtil.getAvatarId(user.avatar)
    val isCustomAvatar = user.avatar.startsWith(Environment.getExternalStorageDirectory().toString())
    Row(modifier.padding(8.dp)) {
        Image(painter = if (isCustomAvatar) rememberAsyncImagePainter(user.avatar) else painterResource(id = avatar), contentDescription = "", Modifier.padding(end = 8.dp).clip(CircleShape).size(40.dp, 40.dp))
        Column(Modifier.weight(1f).align(CenterVertically)) {
            Text(text = user.name, color = MaterialTheme.colors.onBackground)
            if (user.status != null && (!user.status.type.isNullOrEmpty() || !user.status.name.isNullOrEmpty())) Text(text = GeneralUtil.getUserStatusText(user.status), color = MaterialTheme.colors.onBackground, fontSize = 14.sp, modifier = Modifier.alpha(0.7f))
        }
    }
}