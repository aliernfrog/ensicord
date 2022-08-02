package com.aliernfrog.ensicord.util

import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import coil.compose.rememberAsyncImagePainter
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.UserStatus

class GeneralUtil {
    companion object {
        @Composable
        fun getAvatarPainter(avatar: String): Painter {
            val isCustomAvatar = avatar.startsWith(Environment.getExternalStorageDirectory().toString())
            if (isCustomAvatar) return rememberAsyncImagePainter(avatar)
            return when(avatar) {
                "ensi" -> painterResource(R.drawable.ensi)
                "system" -> painterResource(R.drawable.ensi)
                "user" -> painterResource(R.drawable.user)
                else -> painterResource(R.drawable.user)
            }
        }

        fun getUserStatusText(status: UserStatus): AnnotatedString {
            var type = status.type ?: "%n"
            if (!type.contains("%n")) type += "%n"
            val split = type.split("%n")
            val before = split[0]
            val after = split[1]
            return buildAnnotatedString {
                append(before)
                if (status.name != null) withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(status.name) }
                append(after)
            }
        }

        fun getUserStatusFromString(type: String?, name: String? = null): UserStatus? {
            return if (type != null || name != null) UserStatus(type, name)
            else null
        }
    }
}