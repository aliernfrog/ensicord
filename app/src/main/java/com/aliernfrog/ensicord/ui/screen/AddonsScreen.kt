package com.aliernfrog.ensicord.ui.screen

import android.content.SharedPreferences
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aliernfrog.ensicord.AddonFetchState
import com.aliernfrog.ensicord.ConfigKey
import com.aliernfrog.ensicord.NavDestinations
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.state.AddonsState
import com.aliernfrog.ensicord.ui.composable.EnsicordAddon
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold
import com.aliernfrog.ensicord.ui.composable.EnsicordButton
import com.aliernfrog.ensicord.util.AddonsUtil
import com.aliernfrog.toptoast.TopToastColorType
import com.aliernfrog.toptoast.TopToastManager
import kotlinx.coroutines.launch

@Composable
fun AddonsScreen(topToastManager: TopToastManager, navController: NavController, addonsState: AddonsState, config: SharedPreferences) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            if (addonsState.fetchState != AddonFetchState.ADDONS_DONE) addonsState.fetchAddons(
                config.getStringSet(ConfigKey.KEY_ADDON_REPOS, setOf(ConfigKey.DEFAULT_ADDON_REPO))!!
            )
        }
    }
    EnsicordBaseScaffold(title = context.getString(R.string.addons), navController = navController) {
        when (addonsState.fetchState) {
            AddonFetchState.ADDONS_DONE -> {
                addonsState.addons.forEach { addon ->
                    EnsicordAddon(addon) {
                        AddonsUtil.applyAddon(addon, config) { topToastManager.showToast(context.getString(R.string.info_appliedAddon), iconDrawableId = R.drawable.check, iconTintColorType = TopToastColorType.PRIMARY) }
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