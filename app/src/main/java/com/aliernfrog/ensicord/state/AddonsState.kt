package com.aliernfrog.ensicord.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.aliernfrog.ensicord.AddonFetchState
import com.aliernfrog.ensicord.data.Addon
import com.aliernfrog.ensicord.util.AddonsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddonsState() {
    var addons = ArrayList<Addon>()
    var fetchState by mutableStateOf(AddonFetchState.ADDONS_LOADING)

    suspend fun fetchAddons(repos: Set<String>) {
        withContext(Dispatchers.IO) {
            addons = AddonsUtil.getAddons(repos)
            fetchState = AddonFetchState.ADDONS_DONE
        }
    }
}