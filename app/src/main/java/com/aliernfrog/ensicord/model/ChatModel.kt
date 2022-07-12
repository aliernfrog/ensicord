package com.aliernfrog.ensicord.model

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.data.Channel
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.data.UserStatus
import com.aliernfrog.ensicord.util.EnsiUtil
import com.aliernfrog.ensicord.util.GeneralUtil

class ChatModel(context: Context, config: SharedPreferences): ViewModel() {
    var userUser = User(
        "user",
        config.getString("username", "Some frok")!!,
        "user",
        GeneralUtil.getUserStatusFromString(config.getString("userStatus", null))
    )
    val ensiUser = User("ensi", "Ensi", "ensi", EnsiUtil.getStatus(context), true)
    var users = listOf(userUser,ensiUser)

    private val generalChannel = Channel("#general")
    private val devChannel = Channel("#ensicord-development")
    private val starboardChannel = Channel("#starboard", readOnly = true)
    var chosenChannel by mutableStateOf(generalChannel)
    val channels = listOf(generalChannel,devChannel,starboardChannel)

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