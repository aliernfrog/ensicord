package com.aliernfrog.ensicord.state

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.aliernfrog.ensicord.AddonKey
import com.aliernfrog.ensicord.ConfigKey
import com.aliernfrog.ensicord.Path
import com.aliernfrog.ensicord.data.Channel
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.data.UserStatus
import com.aliernfrog.ensicord.util.EnsiUtil
import com.aliernfrog.ensicord.util.GeneralUtil
import java.io.File

class ChatState(context: Context, config: SharedPreferences, addonConfig: SharedPreferences) {
    private var lastId = 0

    var userUser = User(
        "user",
        config.getString(ConfigKey.KEY_USER_NAME, ConfigKey.DEFAULT_USER_NAME)!!,
        getUserAvatar(),
        GeneralUtil.getUserStatusFromString(config.getString(ConfigKey.KEY_USER_STATUS, null))
    )
    val ensiUser = User("ensi",
        addonConfig.getString(AddonKey.KEY_ENSI_NAME, AddonKey.DEFAULT_ENSI_NAME)!!,
        "ensi",
        EnsiUtil.generateStatus(context),
        true
    )
    var users = listOf(userUser,ensiUser)

    private val generalChannel = Channel("#general")
    private val devChannel = Channel("#ensicord-development")
    private val starboardChannel = Channel("#starboard", readOnly = true)
    var chosenChannel by mutableStateOf(generalChannel)
    val channels = listOf(generalChannel,devChannel,starboardChannel)

    fun getNextId(): Int {
        lastId++
        return lastId
    }

    fun updateUser(newUserName: String? = null, newUserStatus: String? = null) {
        userUser = User(
            "user",
            newUserName ?: userUser.name,
            getUserAvatar(),
            if (newUserStatus != null) UserStatus(newUserStatus) else userUser.status
        )
        users = listOf(userUser,ensiUser)
    }

    fun sendMessage(message: Message, clearInput: Boolean = true, ignoreReadOnly: Boolean = false, onSend: (() -> Unit)? = null) {
        if (chosenChannel.readOnly && !ignoreReadOnly) return
        chosenChannel.messages.add(message)
        if (clearInput) chosenChannel.messageInput.value = ""
        if (onSend != null) onSend()
    }

    fun deleteMessage(message: Message, onDelete: (() -> Unit)? = null) {
        chosenChannel.messages.remove(message)
        if (onDelete != null) onDelete()
    }

    private fun getUserAvatar(): String {
        val userAvatarFile = File("${Environment.getExternalStorageDirectory()}${Path.PATH_AVATAR}")
        return if (userAvatarFile.exists()) userAvatarFile.absolutePath else "user"
    }
}