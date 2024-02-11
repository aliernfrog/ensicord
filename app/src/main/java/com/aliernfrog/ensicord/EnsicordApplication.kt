package com.aliernfrog.ensicord

import android.app.Application
import com.aliernfrog.ensicord.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EnsicordApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EnsicordApplication)
            modules(appModules)
        }
    }
}