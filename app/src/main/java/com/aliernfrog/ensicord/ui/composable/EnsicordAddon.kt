package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Addon

@Composable
fun EnsicordAddon(addon: Addon, onClick: () -> Unit) {
    EnsicordColumnRounded(
        onClick = if (!addon.error) onClick else null,
        color = if (!addon.error) MaterialTheme.colors.secondary else MaterialTheme.colors.error
    ) {
        Text(
            text = if (!addon.error) addon.name else LocalContext.current.getString(R.string.warning_somethingWentWrong),
            fontWeight = FontWeight.Bold, fontSize = 25.sp,
            color = if (!addon.error) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onError,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = addon.description,
            color = if (!addon.error) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onError,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = addon.repo,
            fontSize = 10.sp,
            color = if (!addon.error) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onError,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}