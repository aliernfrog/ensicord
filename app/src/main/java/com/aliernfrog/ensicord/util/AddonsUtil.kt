package com.aliernfrog.ensicord.util

import com.aliernfrog.ensicord.data.Addon
import org.json.JSONArray
import org.json.JSONObject
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

        private fun jsonToAddon(jsonObject: JSONObject): Addon {
            return Addon(
                name = jsonObject.getString("name"),
                description = jsonObject.getString("description")
            )
        }
    }
}