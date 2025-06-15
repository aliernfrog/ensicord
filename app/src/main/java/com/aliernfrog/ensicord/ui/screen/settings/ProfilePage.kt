package com.aliernfrog.ensicord.ui.screen.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.component.expressive.ExpressiveSection
import com.aliernfrog.ensicord.ui.viewmodel.ChatViewModel
import com.aliernfrog.ensicord.util.extension.showSuccessToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfilePage(
    chatViewModel: ChatViewModel = koinViewModel(),
    onNavigateBackRequest: () -> Unit
) {
    var nameInput by rememberSaveable {
        mutableStateOf(chatViewModel.prefs.userName)
    }

    SettingsPageContainer(
        title = stringResource(R.string.settings_profile),
        onNavigateBackRequest = onNavigateBackRequest
    ) {
        ExpressiveSection(
            title = stringResource(R.string.avatar),
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = chatViewModel.user.avatarModel,
                contentDescription = stringResource(R.string.settings_profile_avatar),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                onClick = {
                    // TODO
                }
            ) {
                Text(stringResource(R.string.settings_profile_avatar_update))
            }
        }
        ExpressiveSection(stringResource(R.string.settings_profile_name)) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                placeholder = { Text(chatViewModel.prefs.userName) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            Button(
                enabled = nameInput.isNotEmpty() && nameInput != chatViewModel.prefs.userName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                onClick = {
                    chatViewModel.prefs.userName = nameInput
                    chatViewModel.user = chatViewModel.user.copy(
                        name = nameInput
                    )
                    chatViewModel.topToastState.showSuccessToast(
                        text = R.string.settings_profile_name_updated
                    )
                }
            ) {
                Text(stringResource(R.string.settings_profile_name_update))
            }
        }
    }
}