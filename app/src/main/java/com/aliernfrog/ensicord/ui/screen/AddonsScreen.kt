package com.aliernfrog.ensicord.ui.screen

import android.content.SharedPreferences
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aliernfrog.ensicord.AddonFetchingState
import com.aliernfrog.ensicord.NavDestinations
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.model.AddonsModel
import com.aliernfrog.ensicord.ui.composable.EnsicordAddon
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold
import com.aliernfrog.ensicord.ui.composable.EnsicordButton
import com.aliernfrog.ensicord.ui.composable.TopToastManager
import com.aliernfrog.ensicord.util.AddonsUtil

@Composable
fun AddonsScreen(topToastManager: TopToastManager, navController: NavController, addonsModel: AddonsModel, config: SharedPreferences) {
    val context = LocalContext.current
    EnsicordBaseScaffold(title = context.getString(R.string.addons), navController = navController) {
        when (addonsModel.state) {
            AddonFetchingState.ADDONS_DONE -> {
                addonsModel.addons.forEach { addon ->
                    EnsicordAddon(addon) {
                        AddonsUtil.applyAddon(addon, config) { topToastManager.showToast(context.getString(R.string.info_appliedAddon)) }
                    }
                }
            }
            else -> CircularProgressIndicator(Modifier.align(CenterHorizontally).padding(top = 10.dp))
        }
        ManageRepos(navController)
    }
}

@Composable
private fun ManageRepos(navController: NavController) {
    val context = LocalContext.current
    EnsicordButton(title = context.getString(R.string.addonsRepos), painter = painterResource(id = R.drawable.folder)) {
        navController.navigate(NavDestinations.ADDONS_REPOS)
    }
}