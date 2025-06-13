package com.aliernfrog.ensicord.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.enum.Destination
import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.aliernfrog.toptoast.state.TopToastState

class MainViewModel(
    val topToastState: TopToastState,
    val prefs: PreferenceManager
) : ViewModel() {
    val navigationBackStack = mutableStateListOf<Any>(
        Destination.CHAT
    )

    val forceDarkTheme: Boolean
        get() = navigationBackStack.last() == Destination.REELS
}