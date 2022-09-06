package com.aliernfrog.ensicord.ui.screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aliernfrog.ensicord.ConfigKey
import com.aliernfrog.ensicord.NavDestinations
import com.aliernfrog.ensicord.Path
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.model.ChatModel
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold
import com.aliernfrog.ensicord.ui.composable.EnsicordColumnRounded
import com.aliernfrog.ensicord.ui.composable.EnsicordTextField
import com.aliernfrog.ensicord.util.GeneralUtil
import com.aliernfrog.toptoast.TopToastColorType
import com.aliernfrog.toptoast.TopToastManager
import java.io.File

@Composable
fun ProfileScreen(chatModel: ChatModel, topToastManager: TopToastManager, navController: NavController, config: SharedPreferences) {
    val context = LocalContext.current
    EnsicordBaseScaffold(title = context.getString(R.string.profile), navController = navController) {
        ProfileCustomization(chatModel, topToastManager, navController, config)
    }
}

@Composable
private fun ProfileCustomization(chatModel: ChatModel, topToastManager: TopToastManager, navController: NavController, config: SharedPreferences) {
    val context = LocalContext.current
    var username by remember { mutableStateOf(config.getString(ConfigKey.KEY_USER_NAME, ConfigKey.DEFAULT_USER_NAME)!!) }
    var status by remember { mutableStateOf(config.getString(ConfigKey.KEY_USER_STATUS, "")!!) }
    EnsicordColumnRounded {
        AvatarCustomization(chatModel, topToastManager, navController, Modifier.align(CenterHorizontally))
        Spacer(Modifier.height(20.dp))
        EnsicordTextField(
            label = { Text(context.getString(R.string.profileName)) },
            value = username,
            onValueChange = {
                username = it
                config.edit().putString(ConfigKey.KEY_USER_NAME, it).apply()
                chatModel.updateUser(newUserName = it)
            }
        )
        EnsicordTextField(
            label = { Text(context.getString(R.string.profileStatus)) },
            value = status,
            onValueChange = {
                status = it
                config.edit().putString("userStatus", it).apply()
                chatModel.updateUser(newUserStatus = it)
            }
        )
    }
}

@Composable
private fun AvatarCustomization(chatModel: ChatModel, topToastManager: TopToastManager, navController: NavController, modifier: Modifier) {
    val context = LocalContext.current
    val avatar = GeneralUtil.getAvatarPainter(chatModel.userUser.avatar)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.data?.data != null) setAvatar(context, it.data!!.data!!) {
            chatModel.updateUser()
            topToastManager.showToast(context.getString(R.string.profileAvatarUpdated), iconDrawableId = R.drawable.check, iconBackgroundColorType = TopToastColorType.PRIMARY)
            navController.popBackStack()
            navController.navigate(NavDestinations.PROFILE)
        }
    }
    Image(
        painter = avatar,
        contentDescription = context.getString(R.string.profileAvatar),
        modifier = modifier.clip(CircleShape).size(250.dp, 250.dp).clickable{
            val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*").putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            launcher.launch(intent)
        }
    )
}

private fun setAvatar(context: Context, uri: Uri, onSet: () -> Unit) {
    val file = File("${Environment.getExternalStorageDirectory()}${Path.PATH_AVATAR}")
    val input = context.contentResolver.openInputStream(uri)
    val output = file.outputStream()
    input?.copyTo(output)
    input?.close()
    output.close()
    onSet()
}