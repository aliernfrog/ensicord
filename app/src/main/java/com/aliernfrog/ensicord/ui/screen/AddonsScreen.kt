package com.aliernfrog.ensicord.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.model.AddonsModel
import com.aliernfrog.ensicord.ui.composable.EnsicordAddon
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold

@Composable
fun AddonsScreen(navController: NavController, addonsModel: AddonsModel) {
    val context = LocalContext.current
    EnsicordBaseScaffold(title = context.getString(R.string.addons), navController = navController) {
        if (addonsModel.loaded) {
            addonsModel.addons.forEach { addon ->
                EnsicordAddon(addon)
            }
        } else {
            CircularProgressIndicator(Modifier.align(CenterHorizontally).padding(top = 10.dp))
        }
    }
}