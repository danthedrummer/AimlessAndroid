package org.dandowney.aimless.library.design.toggle

import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import org.dandowney.aimless.library.design.theme.AimlessTheme

object AimlessSwitchColors {

  val default: SwitchColors
    @Composable
    get() = SwitchDefaults.colors(
      checkedThumbColor = AimlessTheme.colors.backgroundPrimary,
      checkedTrackColor = AimlessTheme.colors.iconPrimary,
      checkedIconColor = AimlessTheme.colors.iconPrimary,
      uncheckedThumbColor = AimlessTheme.colors.iconSecondary,
      uncheckedTrackColor = AimlessTheme.colors.backgroundSecondary,
      uncheckedBorderColor = AimlessTheme.colors.iconSecondary,
      uncheckedIconColor = AimlessTheme.colors.backgroundSecondary,
    )
}
