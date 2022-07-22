package com.aliernfrog.ensicord

object AddonFetchingState {
    const val ADDONS_LOADING = 0
    const val ADDONS_DONE = 1
    const val ADDONS_ERROR = 2
}

object ConfigKey {
    const val PREF_NAME = "APP_CONFIG"
    const val KEY_APP_THEME = "appTheme"
    const val KEY_USER_NAME = "userName"
    const val KEY_USER_STATUS = "userStatus"
    const val DEFAULT_USER_NAME = "Some frok"
}

object Theme {
    const val SYSTEM = 0
    const val LIGHT = 1
    const val DARK = 2
}