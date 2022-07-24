package com.aliernfrog.ensicord.ui.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.aliernfrog.ensicord.R

@Composable
fun EnsicordAddonRepo(url: String, onRemove: () -> Unit) {
    val context = LocalContext.current
    EnsicordColumnRounded {
        Text(text = url)
        EnsicordButton(title = context.getString(R.string.action_remove), onClick = onRemove)
    }
}