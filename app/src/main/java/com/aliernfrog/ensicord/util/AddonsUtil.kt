package com.aliernfrog.ensicord.util

import android.content.SharedPreferences
import android.util.Log
import com.aliernfrog.ensicord.AddonConstants
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

        fun applyAddon(addon: Addon, config: SharedPreferences, onApply: () -> Unit) {
            val configEdit = config.edit()
            if (addon.setAppTheme != null) configEdit.putInt(ConfigKey.KEY_APP_THEME, Theme[addon.setAppTheme])
            if (addon.setEnsiUserName != null) configEdit.putString(ConfigKey.KEY_ENSI_NAME, addon.setEnsiUserName)
            AddonConstants.SET_METHODS.forEach { method ->
                val set = addon.getSet("set${method.fieldName}")
                val add = addon.getSet("add${method.fieldName}")
                if (set != null) {
                    Log.d("AddonUtil", "applyAddon: setting ${method.prefsKey} to: $set")
                    configEdit.putStringSet(method.prefsKey, set)
                }
                if (add != null) {
                    Log.d("AddonsUtil", "applyAddon: adding to ${method.prefsKey}: $add")
                    appendPrefsSet(method.prefsKey, add, config, configEdit)
                }
            }
            configEdit.apply()
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