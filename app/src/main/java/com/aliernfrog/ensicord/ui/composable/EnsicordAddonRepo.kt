package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.R

@Composable
fun EnsicordAddonRepo(url: String, onRemove: () -> Unit) {
    val context = LocalContext.current
    EnsicordColumnRounded {
        Text(text = url, modifier = Modifier.padding(horizontal = 8.dp))
        EnsicordButton(title = context.getString(R.string.action_remove), backgroundColor = MaterialTheme.colors.error, contentColor = MaterialTheme.colors.onError, onClick = onRemove)
    }
}