package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.R

@Composable
fun EnsicordAddonRepo(url: String, onRemove: () -> Unit) {
    val context = LocalContext.current
    EnsicordColumnRounded {
        SelectionContainer { Text(text = url, modifier = Modifier.padding(horizontal = 8.dp)) }
        EnsicordButton(title = context.getString(R.string.action_remove), painter = painterResource(id = R.drawable.trash), backgroundColor = MaterialTheme.colors.error, contentColor = MaterialTheme.colors.onError, onClick = onRemove)
    }
}