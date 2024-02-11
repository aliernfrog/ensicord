package com.aliernfrog.ensicord.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.ui.component.RadioButtons
import com.aliernfrog.ensicord.ui.component.form.ExpandableRow
import com.aliernfrog.ensicord.ui.component.form.SwitchRow
import com.aliernfrog.ensicord.ui.theme.Theme
import com.aliernfrog.ensicord.ui.theme.supportsMaterialYou
import com.aliernfrog.ensicord.ui.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppearancePage(
    mainViewModel: MainViewModel = koinViewModel()
) {
    var themesExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ExpandableRow(
            expanded = themesExpanded,
            title = stringResource(R.string.settings_appearance_theme),
            description = stringResource(R.string.settings_appearance_theme_description),
            painter = rememberVectorPainter(Icons.Outlined.ModeNight),
            onClickHeader = { themesExpanded = !themesExpanded }
        ) {
            RadioButtons(
                options = Theme.entries.map {
                    stringResource(it.label)
                },
                selectedOptionIndex = mainViewModel.prefs.theme,
                onSelect = { mainViewModel.prefs.theme = it }
            )
        }
        if (supportsMaterialYou) SwitchRow(
            title = stringResource(R.string.settings_appearance_materialYou),
            description = stringResource(R.string.settings_appearance_materialYou_description),
            painter = rememberVectorPainter(Icons.Outlined.Brush),
            checked = mainViewModel.prefs.materialYou
        ) {
            mainViewModel.prefs.materialYou = it
        }
    }
}