package com.aliernfrog.ensicord.impl

import com.aliernfrog.ensicord.data.AddonJSON
import com.aliernfrog.ensicord.data.AddonMetadataJSON

class Addon(
    data: Any
) {
    private val json: AddonJSON
    val meta: AddonMetadataJSON

    init {
        json = when (data) {
            is AddonJSON -> data
            is AddonMetadataJSON -> AddonJSON(meta = data)
            else -> throw IllegalArgumentException("Expected AddonJSON or AddonMetadataJSON")
        }
        meta = json.meta
    }

    val thumbnailURL = meta.thumbnail?.let { thumbnailPath ->
        "${meta.repo}/$thumbnailPath"
    }
}