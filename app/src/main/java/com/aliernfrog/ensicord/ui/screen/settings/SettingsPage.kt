package com.aliernfrog.ensicord.ui.screen.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.component.form.ButtonRow
import com.aliernfrog.ensicord.ui.theme.AppComponentShape
import com.aliernfrog.ensicord.ui.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsPage(
    chatViewModel: ChatViewModel = koinViewModel(),
    onNavigateRequest: (SettingsDestination) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                onClick = { onNavigateRequest(SettingsDestination.PROFILE) },
                shape = AppComponentShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 12.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = chatViewModel.user.avatarModel,
                        contentDescription = stringResource(R.string.avatar),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .size(60.dp)
                    )
                    Column {
                        Text(
                            text = chatViewModel.user.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.settings_profile_description),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            SettingsDestination.entries
                .filter { it.showInPagesList }
                .forEach { destination ->
                    ButtonRow(
                        title = stringResource(destination.title),
                        description = destination.description?.let { stringResource(it) },
                        painter = destination.icon?.let { rememberVectorPainter(it) }
                    ) {
                        onNavigateRequest(destination)
                    }
                }
        }
    }
}