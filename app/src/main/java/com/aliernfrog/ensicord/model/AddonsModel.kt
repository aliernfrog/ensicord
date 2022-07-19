package com.aliernfrog.ensicord.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliernfrog.ensicord.AddonsConstants
import com.aliernfrog.ensicord.data.Addon
import com.aliernfrog.ensicord.util.AddonsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class AddonsModel: ViewModel() {
    var addons = ArrayList<Addon>()
    var state by mutableStateOf(AddonsConstants.ADDONS_LOADING)
    var error by mutableStateOf("")

    fun fetchAddons() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addons = AddonsUtil.getAddons()
                state = AddonsConstants.ADDONS_DONE
            } catch(e: Exception) {
                error = e.toString()
                state = AddonsConstants.ADDONS_ERROR
            }
        }
    }
}