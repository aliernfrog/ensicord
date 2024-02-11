package com.aliernfrog.ensicord.di

import com.aliernfrog.ensicord.ui.viewmodel.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    singleOf(::MainViewModel)
    singleOf(::InsetsViewModel)
    singleOf(::ChatViewModel)
}