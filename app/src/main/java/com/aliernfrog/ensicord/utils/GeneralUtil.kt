package com.aliernfrog.ensicord.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.UserStatus

class GeneralUtil {
    companion object {
        fun getAvatarId(avatar: String): Int {
            return when(avatar) {
                "ensi" -> R.drawable.ensi
                "system" -> R.drawable.system
                "user" -> R.drawable.user
                else -> R.drawable.user
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
    }
}