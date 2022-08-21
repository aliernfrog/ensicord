package com.aliernfrog.ensicord.ui.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.Message
import com.aliernfrog.ensicord.data.User
import com.aliernfrog.ensicord.ui.composable.EnsicordMenuOption
import com.aliernfrog.ensicord.ui.composable.EnsicordMessage
import com.aliernfrog.ensicord.ui.composable.EnsicordModalBottomSheet
import com.aliernfrog.toptoast.TopToastColorType
import com.aliernfrog.toptoast.TopToastManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessageSheet(message: Message?, sheetState: ModalBottomSheetState, topToastManager: TopToastManager, onUserSheetRequest: (User) -> Unit) {
    if (message != null) {
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current
        val scope = rememberCoroutineScope()
        EnsicordModalBottomSheet(sheetState = sheetState) {
            Column(Modifier.heightIn(0.dp, 200.dp).clip(RoundedCornerShape(20.dp))) {
                EnsicordMessage(message = message, onAvatarClick = {
                    onUserSheetRequest(message.author)
                    scope.launch { sheetState.hide() }
                })
            }
            EnsicordMenuOption(text = context.getString(R.string.messageCopy), painterResource(R.drawable.copy_white)) {
                clipboardManager.setText(AnnotatedString(message.content))
                topToastManager.showToast(context.getString(R.string.info_copied), iconDrawableId = R.drawable.copy_white, iconBackgroundColorType = TopToastColorType.PRIMARY)
                scope.launch { sheetState.hide() }
            }
        }
    }
}