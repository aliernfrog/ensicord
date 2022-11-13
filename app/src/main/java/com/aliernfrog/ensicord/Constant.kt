package com.aliernfrog.ensicord

object AddonFetchState {
    const val ADDONS_LOADING = 0
    const val ADDONS_DONE = 1
}

object AddonConstants {
    val COLLECTION_PREF_KEYS = listOf(
        "ensiWords",
        "ensiVerbs",
        "ensiTimes",
        "ensiChars",
        "ensiPlaces",
        "ensiConcs",
        "ensiEmotions",
        "ensiOthers",
        "ensiPositions",
        "ensiTypesNormal",
        "ensiTypesQuestion",
        "ensiTypesStarting"
    )
    const val COLLECTION_METHOD_SET = "set"
    const val COLLECTION_METHOD_ADD = "add"
}

object AddonKey {
    const val PREF_NAME = "APP_ADDON"
    const val KEY_ENSI_NAME = "ensiName"
    const val KEY_ENSI_WORDS = "ensiWords"
    const val KEY_ENSI_VERBS = "ensiVerbs"
    const val KEY_ENSI_TIMES = "ensiTimes"
    const val KEY_ENSI_CHARS = "ensiChars"
    const val KEY_ENSI_PLACES = "ensiPlaces"
    const val KEY_ENSI_CONCS = "ensiConcs"
    const val KEY_ENSI_EMOTIONS = "ensiEmotions"
    const val KEY_ENSI_OTHERS = "ensiOthers"
    const val KEY_ENSI_POSITIONS = "ensiPositions"
    const val KEY_ENSI_TYPES_NORMAL = "ensiTypesNormal"
    const val KEY_ENSI_TYPES_QUESTION = "ensiTypesQuestion"
    const val KEY_ENSI_TYPES_STARTING = "ensiTypesStarting"
    const val DEFAULT_ENSI_NAME = "Ensi"
}

object ChatConstants {
    const val MESSAGE_CHAR_LIMIT = 4000
}

object ConfigKey {
    const val PREF_NAME = "APP_CONFIG"
    const val KEY_APP_THEME = "appTheme"
    const val KEY_USER_NAME = "userName"
    const val KEY_USER_STATUS = "userStatus"
    const val KEY_ADDON_REPOS = "addonRepos"
    const val DEFAULT_USER_NAME = "Some frok"
    val DEFAULT_ADDON_REPOS = setOf("https://aliernfrog.github.io/ensicord-addons/addons.json")
}

object NavDestinations {
    const val CHAT = "chat"
    const val PROFILE = "profile"
    const val ADDONS = "addons"
    const val ADDONS_REPOS = "addonsRepos"
    const val OPTIONS = "options"
}

object Path {
    const val PATH_DATA = "/Android/data/com.aliernfrog.ensicord/files"
    const val PATH_AVATAR = "$PATH_DATA/avatar.png"
}

object Theme {
    operator fun get(appTheme: String): Int {
        return when (appTheme) {
            "DARK" -> DARK
            "LIGHT" -> LIGHT
            else -> SYSTEM
        }
    }

    const val SYSTEM = 0
    const val LIGHT = 1
    const val DARK = 2
}