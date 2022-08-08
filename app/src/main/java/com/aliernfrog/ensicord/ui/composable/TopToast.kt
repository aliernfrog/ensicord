package com.aliernfrog.ensicord.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import kotlin.concurrent.schedule

class TopToastManager {
    var isShowing = mutableStateOf(false)
    var text = mutableStateOf("")
    var icon: Painter? = null
    var iconBackgroundColor: Color = Color.Transparent

    private val timer = Timer()
    private var task: TimerTask? = null

    fun showToast(textToShow: String, iconToShow: Painter? = null, iconBackground: Color = Color.Transparent, stayMs: Long = 3000) {
        task?.cancel()
        timer.purge()
        text.value = textToShow
        icon = iconToShow
        iconBackgroundColor = iconBackground
        isShowing.value = true
        task = timer.schedule(stayMs) { isShowing.value = false }
    }
}

@Composable
fun TopToastBase(
    content: @Composable () -> Unit,
    backgroundColor: Color = Color.Transparent,
    manager: TopToastManager = TopToastManager()
) {
    Box(Modifier.fillMaxSize().background(backgroundColor)) {
        content()
        AnimatedVisibility(manager.isShowing.value) {
            TopToast(manager)
        }
    }
}

@Composable
fun TopToast(manager: TopToastManager) {
    Column(Modifier.fillMaxWidth().padding(top = 24.dp).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.clip(RoundedCornerShape(50.dp)).background(MaterialTheme.colors.background).border(1.dp, MaterialTheme.colors.secondary, RoundedCornerShape(50.dp)).padding(16.dp).animateContentSize()) {
            if (manager.icon != null) Image(
                painter = manager.icon!!,
                contentDescription = manager.text.value,
                Modifier.padding(end = 8.dp).size(25.dp).clip(CircleShape).background(manager.iconBackgroundColor).padding(5.dp).align(Alignment.CenterVertically)
            )
            Text(
                manager.text.value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}