package com.aliernfrog.ensicord.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.viewmodel.ReelsViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReelsScreen(
    viewModel: ReelsViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { viewModel.images.size }
    val images = viewModel.images
    val fetching = viewModel.fetching

    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        if (images.isEmpty()) {
            if (fetching) LoadingIndicator(
                Modifier.align(Alignment.Center)
            ) else Text(
                stringResource(R.string.reels_noImages)
            )
        } else VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            images.getOrNull(pageIndex)?.let {
                ReelPage(it, Modifier.fillMaxSize())
            }
        }
    }

    LaunchedEffect(pagerState.currentPage, images.size) {
        scope.launch {
            if (images.isNotEmpty() && !fetching) {
                if (pagerState.currentPage >= images.size-2) {
                    viewModel.fetchAndAppendNewImages()
                }
            } else if (images.isEmpty() && !fetching) {
                viewModel.fetchAndAppendNewImages()
            }
        }
    }
}

@Composable
fun ReelPage(
    imageBuffer: ByteArray,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageBuffer,
        contentDescription = null,
        modifier = modifier
    )
}