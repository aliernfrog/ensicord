package com.aliernfrog.ensicord.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.TAG
import com.aliernfrog.ensicord.data.AddonMetadataJSON
import com.aliernfrog.ensicord.impl.Addon
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class AddonsViewModel(
    private val gson: Gson
) : ViewModel() {
    // TODO customize repos
    private val repos = listOf(
        "https://aliernfrog.github.io/ensicord-addons"
    )

    var addons = mutableStateListOf<Addon>()
        private set

    suspend fun fetchAddons() {
        withContext(Dispatchers.IO) {
            val all = mutableListOf<Addon>()
            repos.forEach { repo ->
                try {
                    val content = URL("$repo/all").readText()
                    val jsons = gson.fromJson(content, Array<AddonMetadataJSON>::class.java)
                    jsons.forEach {
                        all.add(Addon(it.copy(
                            repo = repo
                        )))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "fetchAddons: failed to fetch repo \"$repo\"", e)
                }
            }
            addons.clear()
            addons.addAll(all)
        }
    }
}