package com.aliernfrog.ensicord.util.manager

import android.content.Context
import com.aliernfrog.ensicord.defaultReleasesURL
import com.aliernfrog.ensicord.ui.theme.Theme
import io.github.aliernfrog.shared.util.manager.BasePreferenceManager

class PreferenceManager(context: Context) : BasePreferenceManager(
    prefs = context.getSharedPreferences("APP_CONFIG", Context.MODE_PRIVATE)
) {
    // Appearance options
    var theme = intPreference("theme", Theme.SYSTEM.int)
    var materialYou = booleanPreference("material_you", true)
    var pitchBlack = booleanPreference("pitch_black", false)

    var userName = stringPreference("user_name", "Somemaus")

    val experimentalOptionsEnabled = booleanPreference("experimental_options_enabled", false)
    val autoCheckUpdates = booleanPreference("auto_updates", true)

    val releasesURL = stringPreference("releases_url", defaultReleasesURL, experimental = true, includeInDebugInfo = false)
}