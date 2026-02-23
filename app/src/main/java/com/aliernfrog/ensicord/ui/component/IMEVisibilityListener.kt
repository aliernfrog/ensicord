package com.aliernfrog.ensicord.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import io.github.aliernfrog.shared.impl.InsetsManager
import org.koin.compose.koinInject

@Composable
fun IMEVisibilityListener(
    onVisibilityUpdate: (visible: Boolean) -> Unit
) {
    val insetsManager = koinInject<InsetsManager>()

    var lastHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(insetsManager.imePadding) {
        val height = insetsManager.imePadding
        if (lastHeight == height) return@LaunchedEffect
        onVisibilityUpdate(height > 0.dp)
        lastHeight = height
    }
}