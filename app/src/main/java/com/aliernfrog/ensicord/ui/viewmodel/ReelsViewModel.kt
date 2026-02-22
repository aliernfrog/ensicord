package com.aliernfrog.ensicord.ui.viewmodel

import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aliernfrog.ensicord.TAG
import com.aliernfrog.ensicord.data.BufferWrapper
import com.aliernfrog.ensicord.util.manager.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ReelsViewModel(
    private val gson: Gson,
    private val prefs: PreferenceManager
) : ViewModel() {
    val images = mutableStateListOf<ByteArray>()
    var fetching by mutableStateOf(false)

    val pagerState = PagerState(0) { images.size }

    suspend fun fetchAndAppendNewImages(count: Int = 3) {
        val buffers = fetchImagesFromAPI(count)
        images.addAll(buffers)
    }

    private suspend fun fetchImagesFromAPI(count: Int): Array<ByteArray> {
        var buffers: Array<ByteArray> = emptyArray()
        fetching = true
        try {
            val wrappers = withContext(Dispatchers.IO) {
                val url = URL("${prefs.reelsURL.value}?count=${count}")
                val response = url.readText()
                gson.fromJson(response, Array<BufferWrapper>::class.java)
            }
            buffers = wrappers.map { wrapper ->
                wrapper.data.map { it.toByte() }.toByteArray()
            }.toTypedArray()
        } catch (e: Exception) {
            Log.d(TAG, "fetchImagesFromAPI: failed to fetch from API", e)
        }
        fetching = false
        return buffers
    }
}