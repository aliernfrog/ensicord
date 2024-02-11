package com.aliernfrog.ensicord.di

import com.aliernfrog.toptoast.state.TopToastState
import org.koin.dsl.module

val appModule = module {
    single {
        TopToastState(
            composeView = null,
            appTheme = null,
            allowSwipingByDefault = false
        )
    }
}