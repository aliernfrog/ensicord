package com.aliernfrog.ensicord.util

import android.content.SharedPreferences
import android.util.Log
import com.aliernfrog.ensicord.AddonConstants
import com.aliernfrog.ensicord.AddonKey
import com.aliernfrog.ensicord.ConfigKey
import com.aliernfrog.ensicord.Theme
import com.aliernfrog.ensicord.data.Addon
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class AddonsUtil {
    companion object {
        fun getAddons(repos: Set<String>): ArrayList<Addon> {
            val addonList = ArrayList<Addon>()
            repos.forEach { url ->
                try {
                    val content = URL(url).readText()
                    val jsonArray = JSONArray(content)
                    for (i in 0 until jsonArray.length()) {
                        addonList.add(jsonToAddon(jsonArray.getJSONObject(i), url))
                    }
                } catch (e: Exception) {
                    addonList.add(Addon(name = "", description = e.toString(), repo = url, error = true))
                }
            }
            return addonList
        }

        fun applyAddon(addon: Addon, config: SharedPreferences, addonConfig: SharedPreferences, onApply: () -> Unit) {
            val configEdit = config.edit()
            val addonConfigEdit = addonConfig.edit()
            if (addon.setAppTheme != null) configEdit.putInt(ConfigKey.KEY_APP_THEME, Theme[addon.setAppTheme])
            if (addon.setEnsiUserName != null) addonConfigEdit.putString(AddonKey.KEY_ENSI_NAME, addon.setEnsiUserName)
            addon.collectionMethods?.forEach { collectionMethod ->
                Log.d("", "applyAddon: ${collectionMethod.key}")
                if (AddonConstants.COLLECTION_PREF_KEYS.contains(collectionMethod.key)) when(collectionMethod.type) {
                    AddonConstants.COLLECTION_METHOD_SET -> addonConfigEdit.putStringSet(collectionMethod.key, collectionMethod.collection)
                    AddonConstants.COLLECTION_METHOD_ADD -> appendPrefsSet(collectionMethod.key, collectionMethod.collection, addonConfig, addonConfigEdit)
                }
            }
            configEdit.apply()
            addonConfigEdit.apply()
            onApply()
        }

        private fun appendPrefsSet(key: String, toAppend: Set<String>, config: SharedPreferences, configEdit: SharedPreferences.Editor) {
            val prev = config.getStringSet(key, setOf())!!
            val filtered = toAppend.filter { !prev.contains(it) }
            configEdit.putStringSet(key, prev.plus(filtered))
        }

        private fun jsonToAddon(jsonObject: JSONObject, fromRepo: String): Addon {
            return try {
                val addon = Gson().fromJson(jsonObject.toString(), Addon::class.java)
                addon.repo = fromRepo
                return addon
            } catch (e: Exception) {
                Addon(name = "", description = e.toString(), repo = fromRepo, error = true)
            }
        }
    }
}