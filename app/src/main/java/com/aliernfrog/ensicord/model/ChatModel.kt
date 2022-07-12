package com.aliernfrog.ensicord.model

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.data.UserStatus
import com.aliernfrog.ensicord.util.EnsiUtil

class ChatModel(context: Context, config: SharedPreferences): ViewModel() {
    private val userStatus = config.getString("userStatus", null)
    val chosenChannel = "#ensicord-development"
    val userUser = User("user", config.getString("username", "Some frok")!!, "user", if (userStatus != null) UserStatus(userStatus) else null)
    val ensiUser = User("ensi", "Ensi", "ensi", EnsiUtil.getStatus(context), true)
    val users = listOf(userUser,ensiUser)
    val channels = listOf("#ensicord-development")
}