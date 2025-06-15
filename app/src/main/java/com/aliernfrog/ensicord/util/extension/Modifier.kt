package com.aliernfrog.ensicord.util.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

fun Modifier.clickableWithColor(
    color: Color,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
): Modifier = this.clickable(
    interactionSource = interactionSource,
    indication = ripple(color = color),
    onClick = onClick
)