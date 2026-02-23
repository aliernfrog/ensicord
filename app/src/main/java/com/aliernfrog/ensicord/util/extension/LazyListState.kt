package com.aliernfrog.ensicord.util.extension

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.isAtBeginning(): Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.firstOrNull() ?: return true
    return lastItem.index == 0
}