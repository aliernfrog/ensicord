package com.aliernfrog.ensicord.utils

import com.aliernfrog.ensicord.R

class GeneralUtil {
    companion object {
        fun getAvatarId(avatar: String): Int {
            return when(avatar) {
                "ensi" -> R.drawable.ensi
                "system" -> R.drawable.system
                "user" -> R.drawable.user
                else -> R.drawable.user
            }
        }
    }
}