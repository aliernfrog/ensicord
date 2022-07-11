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
            val split = status.type?.split("%n")
            val before = split?.get(0) ?: ""
            val after = split?.get(1) ?: ""
            return buildAnnotatedString {
                append(before)
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(status.name) }
                append(after)
            }
        }
    }
}