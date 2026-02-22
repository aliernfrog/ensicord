package com.aliernfrog.ensicord.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.util.appSettingsCategories
import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.aliernfrog.toptoast.state.TopToastState

class SettingsViewModel(
    val prefs: PreferenceManager,
    val topToastState: TopToastState
) : ViewModel() {
    val categories = appSettingsCategories
}