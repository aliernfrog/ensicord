package com.aliernfrog.ensicord.util

import android.content.SharedPreferences
import com.aliernfrog.ensicord.ConfigKey
import com.aliernfrog.ensicord.Theme
import com.aliernfrog.ensicord.data.Addon
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
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
            if (addon.setAppTheme != null) configEdit.putInt(ConfigKey.KEY_APP_THEME, addon.setAppTheme)
            if (addon.setEnsiUserName != null) configEdit.putString(ConfigKey.KEY_ENSI_NAME, addon.setEnsiUserName)
            configEdit.apply()
            onApply()
        }

        private fun jsonToAddon(jsonObject: JSONObject, fromRepo: String): Addon {
            return try {
                Addon(
                    name = jsonObject.getString("name"),
                    description = jsonObject.getString("description"),
                    repo = fromRepo,
                    setAppTheme = stringToAppTheme(getStringOrNull(jsonObject, "setAppTheme")),
                    setEnsiUserName = getStringOrNull(jsonObject, "setEnsiUserName")
                )
            } catch (e: Exception) {
                Addon(name = "", description = e.toString(), repo = fromRepo, error = true)
            }
        }

        private fun getStringOrNull(jsonObject: JSONObject, key: String): String? {
            return try {
                jsonObject.getString(key)
            } catch (e: Exception) {
                null
            }
        }

        private fun stringToAppTheme(string: String?): Int? {
            if (string == null) return null
            return when (string.uppercase()) {
                "SYSTEM" -> Theme.SYSTEM
                "LIGHT" -> Theme.LIGHT
                "DARK" -> Theme.DARK
                else -> null
            }
        }
    }
}