package com.aliernfrog.ensicord.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.model.AddonsModel
import com.aliernfrog.ensicord.ui.composable.EnsicordAddon
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold

@Composable
fun AddonsScreen(navController: NavController, addonsModel: AddonsModel) {
    val context = LocalContext.current
    EnsicordBaseScaffold(title = context.getString(R.string.addons), navController = navController) {
        addonsModel.addons.forEach { addon ->
            EnsicordAddon(addon)
        }
    }
}