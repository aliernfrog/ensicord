package com.aliernfrog.ensicord.di

import com.aliernfrog.ensicord.ui.viewmodel.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    // TODO use viewModelOf
    singleOf(::MainViewModel)
    singleOf(::SettingsViewModel)
    singleOf(::AddonsViewModel)

    singleOf(::ChatViewModel)
    singleOf(::ReelsViewModel)
}