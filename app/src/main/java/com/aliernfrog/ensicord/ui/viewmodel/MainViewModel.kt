package com.aliernfrog.ensicord.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.aliernfrog.toptoast.state.TopToastState

class MainViewModel(
    val topToastState: TopToastState,
    val prefs: PreferenceManager
) : ViewModel()