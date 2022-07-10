package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EnsicordChannel(name: String, chosen: Boolean = false) {
    var backgroundColor = Color.Transparent
    if (chosen) backgroundColor = MaterialTheme.colors.secondaryVariant
    Text(
        text = name,
        color = MaterialTheme.colors.onBackground,
        fontWeight = if (chosen) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.fillMaxWidth().padding(8.dp).clip(RoundedCornerShape(5.dp)).background(backgroundColor).clickable{}.padding(8.dp)
    )
}