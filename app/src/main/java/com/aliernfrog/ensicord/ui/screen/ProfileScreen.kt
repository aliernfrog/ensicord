package com.aliernfrog.ensicord.ui.screen

import android.content.SharedPreferences
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold
import com.aliernfrog.ensicord.ui.composable.EnsicordColumnRounded
import com.aliernfrog.ensicord.ui.composable.EnsicordTextField
import com.aliernfrog.ensicord.util.GeneralUtil

@Composable
fun ProfileScreen(navController: NavController, config: SharedPreferences) {
    val context = LocalContext.current
    EnsicordBaseScaffold(title = context.getString(R.string.profile), navController = navController) {
        ProfileCustomization(config)
        StatusCustomization(config)
    }
}

@Composable
private fun ProfileCustomization(config: SharedPreferences) {
    val context = LocalContext.current
    val avatar = GeneralUtil.getAvatarId("user")
    var username by remember { mutableStateOf(config.getString("username", "Some frok")!!) }
    EnsicordColumnRounded {
        Image(
            painter = painterResource(id = avatar),
            contentDescription = context.getString(R.string.profileAvatar),
            modifier = Modifier.clip(CircleShape).size(250.dp, 250.dp).align(CenterHorizontally).clickable{}
        )
        Spacer(Modifier.height(20.dp))
        EnsicordTextField(
            label = { Text(context.getString(R.string.profileName)) },
            value = username,
            onValueChange = {
                username = it
                config.edit().putString("username", it).apply()
            }
        )
    }
}

@Composable
private fun StatusCustomization(config: SharedPreferences) {
    val context = LocalContext.current
    var status by remember { mutableStateOf(config.getString("userStatus", "")!!) }
    EnsicordTextField(
        label = { Text(context.getString(R.string.profileStatus)) },
        value = status,
        onValueChange = {
            status = it
            config.edit().putString("userStatus", it).apply()
        }
    )
}