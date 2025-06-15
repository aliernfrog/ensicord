package com.aliernfrog.ensicord.util.extension

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import com.aliernfrog.toptoast.enum.TopToastColor
import com.aliernfrog.toptoast.state.TopToastState

fun TopToastState.showSuccessToast(
    text: Any
) {
    this.showToast(
        text = text,
        icon = Icons.Default.Check,
        iconTintColor = TopToastColor.PRIMARY
    )
}