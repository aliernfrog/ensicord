package com.aliernfrog.ensicord.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.aliernfrog.ensicord.*
import com.aliernfrog.ensicord.model.AddonsModel
import com.aliernfrog.ensicord.ui.composable.*
import com.aliernfrog.toptoast.TopToastManager

@Composable
fun OptionsScreen(topToastManager: TopToastManager, navController: NavController, addonsModel: AddonsModel, config: SharedPreferences) {
    val context = LocalContext.current
    EnsicordBaseScaffold(title = context.getString(R.string.options), navController = navController) {
        ThemeSelection(topToastManager, config)
        Addons(navController, addonsModel, config)
    }
}

@Composable
private fun ThemeSelection(topToastManager: TopToastManager, config: SharedPreferences) {
    val context = LocalContext.current
    val checkmark = painterResource(id = R.drawable.check_white)
    val primaryColor = MaterialTheme.colors.primary
    val options = listOf(context.getString(R.string.optionsThemeSystem),context.getString(R.string.optionsThemeLight),context.getString(R.string.optionsThemeDark))
    val chosen = config.getInt(ConfigKey.KEY_APP_THEME, 0)
    EnsicordColumnRounded(color = MaterialTheme.colors.secondary, title = context.getString(R.string.optionsTheme)) {
        EnsicordRadioButtons(options = options, selectedIndex = chosen, columnColor = MaterialTheme.colors.secondaryVariant, onSelect = { option ->
            applyTheme(option, config, context) {
                topToastManager.showToast(context.getString(R.string.optionsThemeChanged), checkmark, primaryColor) { restartApp(context) }
            }
        })
    }
}

@Composable
private fun Addons(navController: NavController, addonsModel: AddonsModel, config: SharedPreferences) {
    val context = LocalContext.current
    EnsicordButton(title = context.getString(R.string.addons), painter = painterResource(id = R.drawable.download)) {
        addonsModel.fetchAddons(config.getStringSet(ConfigKey.KEY_ADDON_REPOS, setOf(ConfigKey.DEFAULT_ADDON_REPO))!!.toSet())
        navController.navigate(NavDestinations.ADDONS)
    }
}

private fun applyTheme(option: String, config: SharedPreferences, context: Context, onApply: () -> Unit) {
    var theme = Theme.SYSTEM
    if (option == context.getString(R.string.optionsThemeLight)) theme = Theme.LIGHT
    if (option == context.getString(R.string.optionsThemeDark)) theme = Theme.DARK
    config.edit().putInt(ConfigKey.KEY_APP_THEME, theme).apply()
    onApply()
}

private fun restartApp(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    (context as Activity).finish()
    context.startActivity(intent)
}