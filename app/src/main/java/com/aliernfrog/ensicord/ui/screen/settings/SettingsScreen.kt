package com.aliernfrog.ensicord.ui.screen.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.rounded.Flare
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.component.NavigateBackButton
import com.aliernfrog.ensicord.ui.component.form.ButtonRow
import com.aliernfrog.ensicord.ui.theme.AppComponentShape
import com.aliernfrog.ensicord.ui.viewmodel.ChatViewModel
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootPage(
    chatViewModel: ChatViewModel = koinViewModel(),
    onNavigateBackRequest: () -> Unit,
    onNavigateRequest: (SettingsDestination) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    NavigateBackButton(onClick = onNavigateBackRequest)
                }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            Card(
                shape = AppComponentShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    onNavigateRequest(SettingsDestination.PROFILE)
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPageContainer(
    title: String,
    onNavigateBackRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    NavigateBackButton(onClick = onNavigateBackRequest)
                }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
            content = content
        )
    }
}

@Composable
private fun SoonTMPage(
    mainViewModel: MainViewModel = koinViewModel(),
    onNavigateBackRequest: () -> Unit
) {
    LaunchedEffect(Unit) {
        mainViewModel.topToastState.showToast(
            text = "Soon™",
            icon = Icons.Rounded.Flare
        )
        onNavigateBackRequest()
    }
}

enum class SettingsDestination(
    @StringRes val title: Int,
    @StringRes val description: Int?,
    val icon: ImageVector?,
    val showInPagesList: Boolean = true,
    val content: @Composable (
        onNavigateBackRequest: () -> Unit,
        onNavigateRequest: (Any) -> Unit
    ) -> Unit
) {
    ROOT(
        title = R.string.settings,
        description = null,
        icon = null,
        showInPagesList = false,
        content = { onNavigateBackRequest, onNavigateRequest ->
            SettingsRootPage(
                onNavigateBackRequest = onNavigateBackRequest,
                onNavigateRequest = onNavigateRequest
            )
        }
    ),

    PROFILE(
        title = R.string.settings_profile,
        description = null,
        icon = null,
        showInPagesList = false,
        content = { onNavigateBackRequest, _ ->
            SoonTMPage(onNavigateBackRequest = onNavigateBackRequest)
        }
    ),

    APPEARANCE(
        title = R.string.settings_appearance,
        description = R.string.settings_appearance_description,
        icon = Icons.Outlined.Palette,
        content = { onNavigateBackRequest, _ ->
            AppearancePage(onNavigateBackRequest = onNavigateBackRequest)
        }
    ),

    ADDONS(
        title = R.string.settings_addons,
        description = R.string.settings_addons_description,
        icon = Icons.Outlined.Download,
        content = { onNavigateBackRequest, _ ->
            AddonsPage(onNavigateBackRequest = onNavigateBackRequest)
        }
    ),

    BACKUP(
        title = R.string.settings_save,
        description = R.string.settings_save_description,
        icon = Icons.Outlined.Backup,
        content = { onNavigateBackRequest, _ ->
            SoonTMPage(onNavigateBackRequest = onNavigateBackRequest)
        }
    ),

    ABOUT(
        title = R.string.settings_about,
        description = R.string.settings_about_description,
        icon = Icons.Outlined.Info,
        content = { onNavigateBackRequest, _ ->
            SoonTMPage(onNavigateBackRequest = onNavigateBackRequest)
        }
    )
}