package com.aliernfrog.ensicord.data.addonMethods

/**
 * Methods that are used to edit a boolean
 */
data class AddonBooleanMethod(
    /**
     * Preferences key of original boolean
     */
    val key: String,

    /**
     * New boolean value
     */
    val boolean: Boolean
)