package com.aliernfrog.ensicord.data

data class Addon(
    val name: String,
    val description: String,
    val error: Boolean = false,
    val setAppTheme: Int? = null,
    val setEnsiUserName: String? = null
)
