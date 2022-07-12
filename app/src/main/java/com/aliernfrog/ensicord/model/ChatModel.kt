package com.aliernfrog.ensicord.model

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.data.UserStatus
import com.aliernfrog.ensicord.util.EnsiUtil
import com.aliernfrog.ensicord.util.GeneralUtil

class ChatModel(context: Context, config: SharedPreferences): ViewModel() {
    val chosenChannel = "#ensicord-development"
    var userUser = User(
        "user",
        config.getString("username", "Some frok")!!,
        "user",
        GeneralUtil.getUserStatusFromString(config.getString("userStatus", null))
    )
    val ensiUser = User("ensi", "Ensi", "ensi", EnsiUtil.getStatus(context), true)
    var users = listOf(userUser,ensiUser)
    val channels = listOf("#ensicord-development")

    fun updateUser(newUserName: String? = null, newUserStatus: String? = null) {
        userUser = User(
            "user",
            newUserName ?: userUser.name,
            "user",
            UserStatus(newUserStatus)
        )
        users = listOf(userUser,ensiUser)
    }
}