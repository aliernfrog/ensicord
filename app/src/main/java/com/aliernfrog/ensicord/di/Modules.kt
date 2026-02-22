package com.aliernfrog.ensicord.di

import com.aliernfrog.ensicord.util.sharedString
import io.github.aliernfrog.shared.di.getSharedModule


val appModules = listOf(
    appModule,
    viewModelModule,

    getSharedModule(
        sharedString = sharedString
    )
)