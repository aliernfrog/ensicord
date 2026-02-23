package com.aliernfrog.ensicord.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.theme.AppComponentShape
import com.aliernfrog.ensicord.ui.viewmodel.ReelsViewModel
import com.aliernfrog.ensicord.util.extension.removeLastIfMultiple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReelsScreen(
    vm: ReelsViewModel = koinViewModel()
) {
    val images = vm.images
    val fetching = vm.fetching

    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        if (images.isEmpty()) {
            if (fetching) LoadingIndicator(
                Modifier.align(Alignment.Center)
            ) else Text(
                stringResource(R.string.reels_noImages)
            )
        } else VerticalPager(
            state = vm.pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            images.getOrNull(pageIndex)?.let {
                ReelPage(it, Modifier.fillMaxSize())
            }
        }

        TopBar(
            onNavigateBackRequest = {
                vm.navigationBackStack.removeLastIfMultiple()
            },
            modifier = Modifier
                .systemBarsPadding()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .fillMaxWidth()
        )
    }

    LaunchedEffect(vm.pagerState.currentPage, images.size) {
        CoroutineScope(Dispatchers.Main).launch {
            if (images.isNotEmpty() && !fetching) {
                if (vm.pagerState.currentPage >= images.size-2) {
                    vm.fetchAndAppendNewImages()
                }
            } else if (images.isEmpty() && !fetching) {
                vm.fetchAndAppendNewImages()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onNavigateBackRequest: () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .clip(AppComponentShape)
                .background(
                    MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f)
                )
                .padding(8.dp)
        ) {
            IconButton(
                onClick = onNavigateBackRequest,
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back)
                )
            }
            Text(
                text = stringResource(R.string.reels),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun ReelPage(
    imageBuffer: ByteArray,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageBuffer,
        contentDescription = null,
        modifier = modifier
    )
}