package com.aliernfrog.ensicord.util.manager

import android.content.Context
import com.aliernfrog.ensicord.ui.theme.Theme
import com.aliernfrog.ensicord.util.manager.base.BasePreferenceManager

class PreferenceManager(context: Context) : BasePreferenceManager(
    prefs = context.getSharedPreferences("APP_CONFIG", Context.MODE_PRIVATE)
) {
    // Appearance options
    var theme by intPreference("theme", Theme.SYSTEM.int)
    var materialYou by booleanPreference("materialYou", true)
}