package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.data.Addon

@Composable
fun EnsicordAddon(addon: Addon, onClick: () -> Unit) {
    EnsicordColumnRounded(onClick = onClick) {
        Text(addon.name, fontWeight = FontWeight.Bold, fontSize = 25.sp, modifier = Modifier.padding(horizontal = 8.dp))
        Text(addon.description, modifier = Modifier.padding(horizontal = 8.dp))
    }
}