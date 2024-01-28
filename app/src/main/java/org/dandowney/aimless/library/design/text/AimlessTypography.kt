package org.dandowney.aimless.library.design.text

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import org.dandowney.aimless.library.design.theme.AimlessTheme

object AimlessTypography {

  private val defaultTypography = Typography()

  val h1: TextStyle
    @Composable
    get() = defaultTypography.h1.copy(color = AimlessTheme.colors.textPrimary)

  val h2: TextStyle
    @Composable
    get() = defaultTypography.h2.copy(color = AimlessTheme.colors.textPrimary)

  val h3: TextStyle
    @Composable
    get() = defaultTypography.h3.copy(color = AimlessTheme.colors.textPrimary)

  val h4: TextStyle
    @Composable
    get() = defaultTypography.h4.copy(color = AimlessTheme.colors.textPrimary)

  val h5: TextStyle
    @Composable
    get() = defaultTypography.h5.copy(color = AimlessTheme.colors.textPrimary)

  val h6: TextStyle
    @Composable
    get() = defaultTypography.h6.copy(color = AimlessTheme.colors.textPrimary)

  val subtitle1: TextStyle
    @Composable
    get() = defaultTypography.subtitle1.copy(color = AimlessTheme.colors.textPrimary)

  val subtitle2: TextStyle
    @Composable
    get() = defaultTypography.subtitle2.copy(color = AimlessTheme.colors.textPrimary)

  val body1: TextStyle
    @Composable
    get() = defaultTypography.body1.copy(color = AimlessTheme.colors.textPrimary)

  val body2: TextStyle
    @Composable
    get() = defaultTypography.body2.copy(color = AimlessTheme.colors.textPrimary)

  val button: TextStyle
    @Composable
    get() = defaultTypography.button.copy(color = AimlessTheme.colors.textPrimary)

  val caption: TextStyle
    @Composable
    get() = defaultTypography.caption.copy(color = AimlessTheme.colors.textPrimary)

  val overline: TextStyle
    @Composable
    get() = defaultTypography.overline.copy(color = AimlessTheme.colors.textPrimary)
}

val LocalTypography = compositionLocalOf { AimlessTypography }
