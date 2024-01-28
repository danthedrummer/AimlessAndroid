package org.dandowney.aimless.feature.color.slider.composable

import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import org.dandowney.aimless.library.design.color.DarkColorPalette
import org.dandowney.aimless.library.design.color.LightColorPalette


val lightSliderColors
  @Composable
  get() = SliderDefaults.colors(
    thumbColor = LightColorPalette.iconPrimary,
    activeTrackColor = LightColorPalette.iconPrimary.copy(alpha = 0.5F),
    inactiveTrackColor = LightColorPalette.backgroundPrimary.copy(alpha = 0.5F),
  )

val darkSliderColors
  @Composable
  get() = SliderDefaults.colors(
    thumbColor = DarkColorPalette.iconPrimary,
    activeTrackColor = DarkColorPalette.iconPrimary.copy(alpha = 0.5F),
    inactiveTrackColor = DarkColorPalette.backgroundPrimary.copy(alpha = 0.5F),
  )

