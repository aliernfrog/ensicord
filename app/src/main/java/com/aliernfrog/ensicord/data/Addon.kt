package com.aliernfrog.ensicord.data

data class Addon(
    val name: String,
    val description: String,
    val setAppTheme: Int? = null,
    val setEnsiUserName: String? = null
)
