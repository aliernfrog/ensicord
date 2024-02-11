package com.aliernfrog.ensicord.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.component.IMEVisibilityListener
import com.aliernfrog.ensicord.ui.component.chat.Message
import com.aliernfrog.ensicord.ui.theme.AppComponentShape
import com.aliernfrog.ensicord.ui.viewmodel.ChatViewModel
import com.aliernfrog.ensicord.util.extension.isAtBeginning
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListLayoutInfo by remember { derivedStateOf { chatViewModel.lazyListState.layoutInfo } }
    var imeVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        chatViewModel.uiScope = scope
    }

    IMEVisibilityListener { isIMEVisible ->
        imeVisible = isIMEVisible
        if (isIMEVisible) scope.launch {
            chatViewModel.lastMessageShownWithoutIME?.let { index ->
                chatViewModel.lazyListState.animateScrollToItem(index)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.alpha(0.92f)
                    ) {
                        Icon(Icons.Default.Tag, null)
                        Text(chatViewModel.chosenChannel.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        /* TODO */
                        chatViewModel.topToastState.showToast(
                            text = "Soon™",
                            icon = Icons.Default.Flare
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.chat_menu)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChatTextInput(
                    modifier = Modifier.weight(1f),
                    value = chatViewModel.textInput,
                    onValueChange = { chatViewModel.textInput = it }
                )
                Crossfade(targetState = chatViewModel.textInput.isNotBlank()) { enabled ->
                    FilledIconButton(
                        onClick = {
                            chatViewModel.sendMessageFromUserInput()
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                        ),
                        enabled = enabled
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.chat_textInput_send)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !chatViewModel.lazyListState.isAtBeginning(),
                modifier = Modifier.padding(16.dp).offset(x = 16.dp, y = 16.dp),
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SmallFloatingActionButton(
                    onClick = { scope.launch {
                        // TODO [bug] this causes top app bar to not change color when scrolling
                        chatViewModel.lazyListState.animateScrollToItem(0)
                    } },
                    shape = RoundedCornerShape(12.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.chat_jumpToBottom)
                    )
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            state = chatViewModel.lazyListState,
            verticalArrangement = Arrangement.aligned(Alignment.Bottom),
            reverseLayout = true
        ) {
            itemsIndexed(chatViewModel.chosenChannel.messages.reversed()) { index, message ->
                if (!imeVisible && lazyListLayoutInfo.visibleItemsInfo.firstOrNull()?.index == index)
                    chatViewModel.lastMessageShownWithoutIME = index
                Message(
                    message = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable { /* TODO */ }
                )
            }
        }
    }
}

@Composable
private fun ChatTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val textFieldContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
    val textFieldTextStyle = LocalTextStyle.current.copy(
        color = contentColorFor(textFieldContainerColor)
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
    ) {
        BasicTextField(
            value = if (enabled) value else "",
            onValueChange = onValueChange,
            textStyle = textFieldTextStyle,
            cursorBrush = SolidColor(TextFieldDefaults.colors().cursorColor),
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .padding(4.dp)
                .clip(AppComponentShape)
                .background(textFieldContainerColor)
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        )
        if (value.isEmpty() || !enabled) Text(
            text = stringResource(
                if (enabled) R.string.chat_textInput_hint else R.string.chat_isReadOnly
            ),
            style = textFieldTextStyle,
            modifier = Modifier
                .padding(start = 20.dp)
                .alpha(0.7f)
        )
    }
}