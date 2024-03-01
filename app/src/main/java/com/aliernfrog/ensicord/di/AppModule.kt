package com.aliernfrog.ensicord.di

import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.aliernfrog.toptoast.state.TopToastState
import com.google.gson.Gson
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::PreferenceManager)
    singleOf(::Gson)
    single {
        TopToastState(
            composeView = null,
            appTheme = null,
            allowSwipingByDefault = false
        )
    }
}