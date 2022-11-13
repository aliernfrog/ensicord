package com.aliernfrog.ensicord.data.addonMethods

/**
 * Methods that are used to edit a collection
 */
data class AddonCollectionMethod(
    /**
     * Type of method
     *
     * Can be "set" or "add"
     *
     * "set" sets the value of original collection to [collection]
     *
     * "add" adds the values of [collection] to the original collection, if it doesn't already exist
     */
    val type: String,

    /**
     * Preferences key of original collection
     */
    val key: String,

    /**
     * New collection to add to original collection or set as collection, depending on [type]
     */
    val collection: Set<String>
)