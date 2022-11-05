package com.aliernfrog.ensicord.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.aliernfrog.ensicord.MainActivity
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.UserStatus

class GeneralUtil {
    companion object {
        @Composable
        fun getStatusBarHeight(): Dp {
            return WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        }

        @Composable
        fun getNavigationBarHeight(): Dp {
            val navigationHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            val keyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current).dp >= navigationHeight
            return if (keyboardVisible) 0.dp else navigationHeight
        }

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

        fun restartApp(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            (context as Activity).finish()
            context.startActivity(intent)
        }
    }
}