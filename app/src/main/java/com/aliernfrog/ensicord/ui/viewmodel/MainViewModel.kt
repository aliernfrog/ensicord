package com.aliernfrog.ensicord.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.domain.AppState
import com.aliernfrog.ensicord.util.Destination
import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.aliernfrog.toptoast.state.TopToastState

class MainViewModel(
    private val appState: AppState,
    val topToastState: TopToastState,
    val prefs: PreferenceManager
) : ViewModel() {
    val navigationBackStack
        get() = appState.navigationBackStack

    val forceDarkTheme: Boolean
        get() = navigationBackStack.last() == Destination.Reels
}