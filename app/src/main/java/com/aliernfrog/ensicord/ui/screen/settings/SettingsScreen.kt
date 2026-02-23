package com.aliernfrog.ensicord.ui.screen.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.SettingsConstant.credits
import com.aliernfrog.ensicord.SettingsConstant.socials
import com.aliernfrog.ensicord.SettingsConstant.supportLinks
import com.aliernfrog.ensicord.ui.viewmodel.SettingsViewModel
import com.aliernfrog.ensicord.util.AppSettingsDestination
import com.aliernfrog.ensicord.util.staticutil.GeneralUtil
import io.github.aliernfrog.shared.ui.screen.settings.AboutPage
import io.github.aliernfrog.shared.ui.screen.settings.AppearancePage
import io.github.aliernfrog.shared.ui.screen.settings.ExperimentalPage
import io.github.aliernfrog.shared.ui.screen.settings.LibsPage
import io.github.aliernfrog.shared.ui.screen.settings.SettingsDestination
import io.github.aliernfrog.shared.ui.screen.settings.SettingsRootPage
import io.github.aliernfrog.shared.util.resolve
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    destination: SettingsDestination,
    vm: SettingsViewModel = koinViewModel(),
    onCheckUpdatesRequest: (skipVersionCheck: Boolean) -> Unit,
    onNavigateUpdatesScreenRequest: () -> Unit,
    onNavigateBackRequest: () -> Unit,
    onNavigateRequest: (SettingsDestination) -> Unit
) {
    val context = LocalContext.current
    //val availableUpdates = vm.versionManager.availableUpdates.collectAsStateWithLifecycle().value

    when (destination) {
        SettingsDestination.root -> {
            SettingsRootPage(
                categories = vm.categories,
                availableUpdates = listOf(), // TODO
                experimentalOptionsEnabled = vm.prefs.experimentalOptionsEnabled.value,
                onShowUpdateSheetRequest = onNavigateUpdatesScreenRequest,
                onNavigateBackRequest = onNavigateBackRequest,
                onNavigateRequest = onNavigateRequest
            )
        }

        AppSettingsDestination.profile -> {
            ProfilePage(
                onNavigateBackRequest = onNavigateBackRequest
            )
        }

        AppSettingsDestination.addons -> {
            AddonsPage(
                onNavigateBackRequest = onNavigateBackRequest
            )
        }

        SettingsDestination.appearance -> {
            AppearancePage(
                themePref = vm.prefs.theme,
                materialYouPref = vm.prefs.materialYou,
                pitchBlackPref = vm.prefs.pitchBlack,
                onNavigateBackRequest = onNavigateBackRequest
            )
        }

        SettingsDestination.experimental -> {
            ExperimentalPage(
                experimentalPrefs = vm.prefs.experimentalPrefs,
                experimentalOptionsEnabledPref = vm.prefs.experimentalOptionsEnabled,
                onCheckUpdatesRequest = onCheckUpdatesRequest,
                onNavigateUpdatesScreenRequest = onNavigateUpdatesScreenRequest,
                onRestartAppRequest = { GeneralUtil.restartApp(context) },
                onNavigateBackRequest = onNavigateBackRequest
            ) {
                // No extra content, for now
            }
        }

        SettingsDestination.about -> {
            AboutPage(
                socials = socials,
                credits = credits,
                supportLinks = supportLinks,
                debugInfo = "", // TODO
                autoCheckUpdatesPref = vm.prefs.autoCheckUpdates,
                experimentalOptionsEnabled = vm.prefs.experimentalOptionsEnabled.value,
                onExperimentalOptionsEnabled = {
                    vm.prefs.experimentalOptionsEnabled.value = true
                    vm.topToastState.showToast(
                        text = "Experimental options enabled",
                        icon = Icons.Rounded.Science
                    )
                },
                onShowUpdateSheetRequest = onNavigateUpdatesScreenRequest,
                onNavigateLibsRequest = { onNavigateRequest(SettingsDestination.libs) },
                onNavigateBackRequest = onNavigateBackRequest
            )
        }

        SettingsDestination.libs -> {
            LibsPage(
                librariesJSONRes = R.raw.aboutlibraries,
                onNavigateBackRequest = onNavigateBackRequest
            )
        }

        else -> {
            Text("UNKNOWN DESTINATION: ${destination.title.resolve()}")
        }
    }
}