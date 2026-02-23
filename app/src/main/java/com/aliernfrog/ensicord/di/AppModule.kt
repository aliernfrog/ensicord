package com.aliernfrog.ensicord.di

import com.aliernfrog.ensicord.BuildConfig
import com.aliernfrog.ensicord.TAG
import com.aliernfrog.ensicord.domain.AppState
import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.aliernfrog.toptoast.state.TopToastState
import com.google.gson.Gson
import io.github.aliernfrog.shared.impl.VersionManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::PreferenceManager)
    singleOf(::Gson)

    single {
        get<PreferenceManager>().let { prefs ->
            @Suppress("KotlinConstantConditions") VersionManager(
                tag = TAG,
                appName = "LAC Tool",
                releasesURLPref = prefs.releasesURL,
                debugInfoPrefs = prefs.debugInfoPrefs,
                defaultInstallURL = "https://github.com/aliernfrog/lac-tool",
                buildCommit = BuildConfig.GIT_COMMIT,
                buildBranch = BuildConfig.GIT_BRANCH,
                buildHasLocalChanges = BuildConfig.GIT_LOCAL_CHANGES,
                context = get()
            )
        }
    }

    singleOf(::AppState)
    single {
        TopToastState(
            composeView = null,
            appTheme = null,
            allowSwipingByDefault = false
        )
    }
}