package org.dandowney.aimless.library.design.modifier

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import org.dandowney.aimless.library.design.color.DarkColorPalette
import org.dandowney.aimless.library.design.color.LightColorPalette
import org.dandowney.aimless.library.design.color.isDark

fun Modifier.aimlessClickable(
  interactionSource: MutableInteractionSource = MutableInteractionSource(),
  shouldPerformHaptics: Boolean = true,
  onClick: () -> Unit,
) = composed {
  val view = LocalView.current
  clickable(
    interactionSource = remember { interactionSource },
    indication = null,
  ) {
    if (shouldPerformHaptics) view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    onClick()
  }
}

fun Modifier.reactiveShadow(
  elevation: Dp,
  surfaceColor: Color,
  shape: Shape,
): Modifier {
  val shadowColor = if (surfaceColor.isDark()) DarkColorPalette.iconPrimary else LightColorPalette.iconPrimary
  return shadow(
    elevation = elevation,
    shape = shape,
    spotColor = shadowColor,
    ambientColor = shadowColor,
  )
}
