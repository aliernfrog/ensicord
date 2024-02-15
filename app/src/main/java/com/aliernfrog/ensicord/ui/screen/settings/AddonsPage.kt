package com.aliernfrog.ensicord.ui.screen.settings

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.impl.Addon
import com.aliernfrog.ensicord.ui.theme.AppComponentShape
import com.aliernfrog.ensicord.ui.viewmodel.AddonsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddonsPage(
    addonsViewModel: AddonsViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        addonsViewModel.fetchAddons()
    }

    LazyColumn {
        items(addonsViewModel.addons) {
            AddonCard(it)
        }
    }
}

@Composable
private fun AddonCard(addon: Addon) {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            ),
        shape = AppComponentShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = { /* TODO */ }
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                addon.thumbnailURL?.let { thumbnailURL ->
                    AsyncImage(
                        model = thumbnailURL,
                        contentDescription = stringResource(R.string.settings_addons_thumbnail),
                        modifier = Modifier.size(50.dp).padding(end = 8.dp)
                    )
                }
                Text(
                    text = addon.meta.name,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = addon.meta.description,
                style = MaterialTheme.typography.bodyMedium
            )

            addon.meta.authors?.let { authors ->
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    authors.forEach { username ->
                        AssistChip(
                            onClick = {
                                uriHandler.openUri("https://github.com/$username")
                            },
                            label = {
                                Text(username)
                            },
                            leadingIcon = {
                                // TODO display avatar
                                Icon(
                                    Icons.Default.Person, null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}