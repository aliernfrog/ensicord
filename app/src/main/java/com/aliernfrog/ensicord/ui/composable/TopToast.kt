package com.aliernfrog.ensicord.ui.composable

import android.os.Handler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TopToastManager {
    var isShowing = mutableStateOf(false)
    var text = mutableStateOf("")

    fun showToast(textToShow: String, stayMs: Long = 3000) {
        text.value = textToShow
        isShowing.value = true
        Handler().postDelayed({ isShowing.value = false }, stayMs)
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
            TopToast(manager.text.value)
        }
    }
}

@Composable
fun TopToast(text: String) {
    Column(Modifier.fillMaxWidth().padding(top = 24.dp).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.clip(RoundedCornerShape(50.dp)).background(MaterialTheme.colors.background).border(3.dp, MaterialTheme.colors.secondary, RoundedCornerShape(50.dp))) {
            Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground, modifier = Modifier.padding(16.dp))
        }
    }
}