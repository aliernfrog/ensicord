package com.aliernfrog.ensicord.di

import com.aliernfrog.ensicord.ui.viewmodel.*
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AddonsViewModel)

    // TODO: Following requires repositories before converting to a viewModelOf
    singleOf(::ChatViewModel)
    singleOf(::ReelsViewModel)
}