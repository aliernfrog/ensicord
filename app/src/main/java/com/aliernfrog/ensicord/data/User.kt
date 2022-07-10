package com.aliernfrog.ensicord.data

data class User(
    val id: String,
    val name: String,
    val avatar: String,
    val status: String? = null,
    val bot: Boolean? = false
)