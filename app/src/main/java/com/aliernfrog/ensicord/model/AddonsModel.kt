package com.aliernfrog.ensicord.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliernfrog.ensicord.data.Addon
import com.aliernfrog.ensicord.util.AddonsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddonsModel: ViewModel() {
    var addons = ArrayList<Addon>()

    fun fetchAddons() {
        viewModelScope.launch(Dispatchers.IO) {
            addons = AddonsUtil.getAddons()
        }
    }
}