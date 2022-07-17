package com.aliernfrog.ensicord.ui.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.data.Addon

@Composable
fun EnsicordAddon(addon: Addon) {
    EnsicordColumnRounded {
        Text(addon.name, fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Text(addon.description)
    }
}