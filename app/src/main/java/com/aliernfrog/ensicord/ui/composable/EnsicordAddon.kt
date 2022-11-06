package com.aliernfrog.ensicord.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Addon

@Composable
fun EnsicordAddon(addon: Addon, onClick: () -> Unit) {
    var showThumbnail by remember { mutableStateOf(true) }
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (addon.error) MaterialTheme.colors.error else MaterialTheme.colors.secondary,
            contentColor = if (addon.error) MaterialTheme.colors.onError else MaterialTheme.colors.onSecondary
        ),
        contentPadding = PaddingValues(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!addon.thumbnailUrl.isNullOrBlank() && showThumbnail) {
                    AsyncImage(
                        model = addon.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp).size(45.dp),
                        onError = { showThumbnail = false }
                    )
                }
                Text(
                    text = if (!addon.error) addon.name else LocalContext.current.getString(R.string.warning_somethingWentWrong),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = addon.description,
                color = if (!addon.error) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onError,
                modifier = Modifier.alpha(0.8f)
            )
            Text(
                text = addon.repo,
                fontSize = 10.sp,
                color = if (!addon.error) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onError,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}