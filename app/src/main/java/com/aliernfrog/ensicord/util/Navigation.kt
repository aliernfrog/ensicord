package com.aliernfrog.ensicord.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.ui.graphics.Color
import androidx.navigation3.ui.NavDisplay
import com.aliernfrog.ensicord.R
import io.github.aliernfrog.shared.ui.screen.settings.SettingsDestination
import io.github.aliernfrog.shared.ui.screen.settings.category
import io.github.aliernfrog.shared.util.SharedStringResolvable

object Destination {
    object Chat
    object Reels
}

class AppSettingsDestination {
    companion object {
        val profile = SettingsDestination(
            title = SharedStringResolvable.Resource(R.string.settings_profile),
            description = SharedStringResolvable.Resource(R.string.settings_profile_description),
            icon = Icons.Rounded.AccountCircle,
            iconContainerColor = Color.Green
        )

        val addons = SettingsDestination(
            title = SharedStringResolvable.Resource(R.string.settings_addons),
            description = SharedStringResolvable.Resource(R.string.settings_addons_description),
            icon = Icons.Rounded.Extension,
            iconContainerColor = Color.Yellow
        )
    }
}

val appSettingsCategories = listOf(
    category(
        title = SharedStringResolvable.Resource(R.string.settings_category_customization)
    ) {
        +AppSettingsDestination.profile
        +AppSettingsDestination.addons
    },

    category(
        title = SharedStringResolvable.Resource(R.string.settings_category_app)
    ) {
        +SettingsDestination.appearance
        +SettingsDestination.experimental
        +SettingsDestination.about
    }
)

val slideTransitionMetadata = NavDisplay.transitionSpec {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Start
    ) + fadeIn() togetherWith slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Start
    ) + fadeOut()
} + NavDisplay.popTransitionSpec {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.End
    ) togetherWith slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.End
    )
} + NavDisplay.predictivePopTransitionSpec {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.End
    ) togetherWith slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.End
    )
}

val slideVerticalTransitionMetadata = NavDisplay.transitionSpec {
    slideInVertically(
        initialOffsetY = { it }
    ) + fadeIn() togetherWith slideOutVertically(
        targetOffsetY = { -it }
    ) + fadeOut()
} + NavDisplay.popTransitionSpec {
    slideInVertically(
        initialOffsetY = { -it }
    ) + fadeIn() togetherWith slideOutVertically(
        targetOffsetY = { -it }
    ) + fadeOut()
} + NavDisplay.predictivePopTransitionSpec {
    slideInVertically(
        initialOffsetY = { -it }
    ) + fadeIn() togetherWith slideOutVertically(
        targetOffsetY = { it }
    ) + fadeOut()
}