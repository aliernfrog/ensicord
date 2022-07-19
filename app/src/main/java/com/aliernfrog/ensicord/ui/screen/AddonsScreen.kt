package com.aliernfrog.ensicord.ui.screen

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aliernfrog.ensicord.AddonFetchingState
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.model.AddonsModel
import com.aliernfrog.ensicord.ui.composable.EnsicordAddon
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold
import com.aliernfrog.ensicord.ui.composable.EnsicordColumnRounded
import com.aliernfrog.ensicord.util.AddonsUtil

@Composable
fun AddonsScreen(navController: NavController, addonsModel: AddonsModel, config: SharedPreferences) {
    val context = LocalContext.current
    EnsicordBaseScaffold(title = context.getString(R.string.addons), navController = navController) {
        when (addonsModel.state) {
            AddonFetchingState.ADDONS_DONE -> {
                addonsModel.addons.forEach { addon ->
                    EnsicordAddon(addon) {
                        AddonsUtil.applyAddon(addon, config) { Toast.makeText(context, context.getString(R.string.info_appliedAddon), Toast.LENGTH_SHORT).show() }
                    }
                }
            }
            AddonFetchingState.ADDONS_ERROR -> ErrorColumn(addonsModel.error)
            else -> CircularProgressIndicator(Modifier.align(CenterHorizontally).padding(top = 10.dp))
        }
    }
}

@Composable
private fun ErrorColumn(error: String) {
    val context = LocalContext.current
    EnsicordColumnRounded(title = context.getString(R.string.warning_somethingWentWrong), color = MaterialTheme.colors.error) {
        Text(text = error, color = MaterialTheme.colors.onError, modifier = Modifier.padding(horizontal = 8.dp))
    }
}