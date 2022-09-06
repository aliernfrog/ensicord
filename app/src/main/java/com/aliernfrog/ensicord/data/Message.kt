package com.aliernfrog.ensicord.data

data class Message(
    val id: Int,
    val author: User,
    val content: String
)