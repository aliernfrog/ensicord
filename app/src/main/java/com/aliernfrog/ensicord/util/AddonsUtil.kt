package com.aliernfrog.ensicord.util

import android.content.SharedPreferences
import com.aliernfrog.ensicord.Theme
import com.aliernfrog.ensicord.data.Addon
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class AddonsUtil {
    companion object {
        private const val addonsUrl = "https://aliernfrog.github.io/ensicord-addons/addons.json"

        fun getAddons(): ArrayList<Addon> {
            val content = URL(addonsUrl).readText()
            val jsonArray = JSONArray(content)
            val addonList = ArrayList<Addon>()
            for (i in 0 until jsonArray.length()) {
                addonList.add(jsonToAddon(jsonArray.getJSONObject(i)))
            }
            return addonList
        }

        fun applyAddon(addon: Addon, config: SharedPreferences, onApply: () -> Unit) {
            val configEdit = config.edit()
            if (addon.setAppTheme != null) configEdit.putInt("appTheme", addon.setAppTheme)
            configEdit.apply()
            onApply()
        }

        private fun jsonToAddon(jsonObject: JSONObject): Addon {
            return Addon(
                name = jsonObject.getString("name"),
                description = jsonObject.getString("description"),
                setAppTheme = stringToAppTheme(getStringOrNull(jsonObject, "setAppTheme"))
            )
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