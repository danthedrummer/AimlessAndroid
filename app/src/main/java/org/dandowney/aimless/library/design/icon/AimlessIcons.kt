package org.dandowney.aimless.library.design.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.dandowney.aimless.R

data object AimlessIcons {

  val back: Painter
    @Composable
    get() = painterResource(id = R.drawable.ic_back)

  val heartOutline: Painter
    @Composable
    get() = painterResource(id = R.drawable.ic_heart_outline)

  val settings: Painter
    @Composable
    get() = painterResource(id = R.drawable.ic_settings)

  val aimless: Painter
    @Composable
    get() = painterResource(id = R.drawable.ic_launcher_foreground)

  val link: Painter
    @Composable
    get() = painterResource(id = R.drawable.ic_link)
}

val LocalIcons = staticCompositionLocalOf { AimlessIcons }
