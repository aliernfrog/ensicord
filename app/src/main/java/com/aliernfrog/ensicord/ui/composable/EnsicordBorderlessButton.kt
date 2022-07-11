package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun EnsicordBorderlessButton(painterLight: Painter, painterDark: Painter, contentDescription: String, onClick: () -> Unit) {
    Image(
        painter = if (MaterialTheme.colors.isLight) painterLight else painterDark,
        contentDescription = contentDescription,
        Modifier.padding(horizontal = 8.dp).size(25.dp, 25.dp).clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = false, radius = 25.dp),
        onClick = onClick
    ).alpha(0.6f))
}