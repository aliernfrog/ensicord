package com.aliernfrog.ensicord.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.ui.viewmodel.InsetsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun IMEVisibilityListener(
    onVisibilityUpdate: (visible: Boolean) -> Unit
) {
    val insetsViewModel = koinViewModel<InsetsViewModel>()

    var lastHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(insetsViewModel.imePadding) {
        val height = insetsViewModel.imePadding
        if (lastHeight == height) return@LaunchedEffect
        onVisibilityUpdate(height > 0.dp)
        lastHeight = height
    }
}