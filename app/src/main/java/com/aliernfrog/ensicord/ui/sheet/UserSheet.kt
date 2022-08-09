package com.aliernfrog.ensicord.ui.sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.ui.composable.EnsicordModalBottomSheet
import com.aliernfrog.ensicord.util.GeneralUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserSheet(user: User? = null, sheetState: ModalBottomSheetState, onNameClick: (() -> Unit)? = null) {
    if (user != null) {
        EnsicordModalBottomSheet(sheetState = sheetState) {
            Image(painter = GeneralUtil.getAvatarPainter(user.avatar), contentDescription = user.name, modifier = Modifier.padding(vertical = 16.dp).size(120.dp).clip(CircleShape).align(Alignment.Start))
            Text(user.name, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 30.sp, modifier = Modifier.fillMaxWidth().clickable { if (onNameClick != null) onNameClick() })
            if (user.status != null && (!user.status.type.isNullOrEmpty() || !user.status.name.isNullOrEmpty())) Text(text = GeneralUtil.getUserStatusText(user.status), color = MaterialTheme.colors.onBackground, fontSize = 14.sp, modifier = Modifier.alpha(0.7f))
        }
    }
}