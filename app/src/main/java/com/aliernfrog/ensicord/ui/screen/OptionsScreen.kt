package com.aliernfrog.ensicord.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.aliernfrog.ensicord.MainActivity
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.composable.EnsicordBaseScaffold
import com.aliernfrog.ensicord.ui.composable.EnsicordColumnRounded
import com.aliernfrog.ensicord.ui.composable.EnsicordRadioButtons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private lateinit var scope: CoroutineScope
private lateinit var scaffoldState: ScaffoldState

@Composable
fun OptionsScreen(navController: NavController, config: SharedPreferences) {
    val context = LocalContext.current
    scope = rememberCoroutineScope()
    scaffoldState = rememberScaffoldState()
    EnsicordBaseScaffold(title = context.getString(R.string.options), state = scaffoldState, navController = navController) {
        ThemeSelection(config)
    }
}

@Composable
private fun ThemeSelection(config: SharedPreferences) {
    val context = LocalContext.current
    val options = listOf(context.getString(R.string.optionsThemeSystem),context.getString(R.string.optionsThemeLight),context.getString(R.string.optionsThemeDark))
    val chosen = config.getInt("appTheme", 0)
    EnsicordColumnRounded(color = MaterialTheme.colors.secondary, title = context.getString(R.string.optionsTheme)) {
        EnsicordRadioButtons(options = options, selectedIndex = chosen, columnColor = MaterialTheme.colors.secondaryVariant, onSelect = { option ->
            applyTheme(option, config, context)
        })
    }
}

private fun applyTheme(option: String, config: SharedPreferences, context: Context) {
    var theme = 0 //system
    if (option == context.getString(R.string.optionsThemeLight)) theme = 1 //light
    if (option == context.getString(R.string.optionsThemeDark)) theme = 2 //dark
    config.edit().putInt("appTheme", theme).apply()
    scope.launch {
        when(scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.optionsThemeChanged), context.getString(R.string.action_restartNow))) {
            SnackbarResult.ActionPerformed -> { restartApp(context) }
            SnackbarResult.Dismissed -> {  }
        }
    }
}

private fun restartApp(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    (context as Activity).finish()
    context.startActivity(intent)
}