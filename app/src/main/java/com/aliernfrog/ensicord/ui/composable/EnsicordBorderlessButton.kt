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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun EnsicordBorderlessButton(painter: Painter, painterTintColor: Color = MaterialTheme.colors.onBackground, contentDescription: String, onClick: () -> Unit) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        alpha = 0.6f,
        colorFilter = ColorFilter.tint(painterTintColor),
        modifier = Modifier.padding(horizontal = 8.dp).size(25.dp, 25.dp).clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false, radius = 20.dp),
            onClick = onClick
        )
    )
}