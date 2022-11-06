package com.aliernfrog.ensicord.ui.screen

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.aliernfrog.ensicord.ConfigKey
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.composable.*

@Composable
fun AddonsReposScreen(navController: NavController, config: SharedPreferences) {
    val context = LocalContext.current
    val repos = remember { mutableStateOf(config.getStringSet(ConfigKey.KEY_ADDON_REPOS, ConfigKey.DEFAULT_ADDON_REPOS)!!.toSet()) }
    EnsicordBaseScaffold(title = context.getString(R.string.addonsRepos), navController) {
        AddRepo(repos.value, config) { repos.value = it }
        Repos(repos.value, config) { repos.value = it }
    }
}

@Composable
private fun AddRepo(repos: Set<String>, config: SharedPreferences, onUpdate: (Set<String>) -> Unit) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    EnsicordColumn(title = context.getString(R.string.addonsReposAdd)) {
        EnsicordTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(context.getString(R.string.addonsReposUrl)) },
            placeholder = { Text(context.getString(R.string.addonsReposPlaceholder)) },
            singleLine = true
        )
        AnimatedVisibility(visible = text.isNotBlank()) {
            EnsicordButton(
                title = context.getString(R.string.action_add),
                painter = painterResource(id = R.drawable.check),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            ) {
                if (text.isNotBlank()) onUpdate(addRepo(text, repos, config))
            }
        }
    }
}

@Composable
private fun Repos(repos: Set<String>, config: SharedPreferences, onUpdate: (Set<String>) -> Unit) {
    repos.forEach { url ->
        EnsicordAddonRepo(url) {
            onUpdate(removeRepo(url, repos, config))
        }
    }
}

private fun addRepo(url: String, repos: Set<String>, config: SharedPreferences): Set<String> {
    val updated = repos.plus(url)
    config.edit().putStringSet(ConfigKey.KEY_ADDON_REPOS, updated).apply()
    return updated
}

private fun removeRepo(url: String, repos: Set<String>, config: SharedPreferences): Set<String> {
    val updated = repos.filter { it != url }.toSet()
    config.edit().putStringSet(ConfigKey.KEY_ADDON_REPOS, updated).apply()
    return updated
}