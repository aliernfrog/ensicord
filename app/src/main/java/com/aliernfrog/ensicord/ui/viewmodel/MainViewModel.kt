package com.aliernfrog.ensicord.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.aliernfrog.toptoast.state.TopToastState

class MainViewModel(
    val topToastState: TopToastState
) : ViewModel()