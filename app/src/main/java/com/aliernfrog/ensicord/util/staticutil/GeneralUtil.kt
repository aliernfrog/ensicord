package com.aliernfrog.ensicord.util.staticutil

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.aliernfrog.ensicord.di.appModules
import com.aliernfrog.ensicord.ui.activity.MainActivity
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class GeneralUtil {
    companion object {
        fun restartApp(context: Context, withModules: Boolean = true) {
            val intent = Intent(context, MainActivity::class.java)
            (context as Activity).finish()
            if (withModules) {
                unloadKoinModules(appModules)
                loadKoinModules(appModules)
            }
            context.startActivity(intent)
        }
    }
}