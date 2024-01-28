package org.dandowney.aimless.feature.color.slider.contract

import androidx.compose.ui.graphics.Color
import org.dandowney.aimless.library.architecture.ViewState

data class ColorSliderState(
  val channels: List<ColorSliderChannelState>,
  val color: Color,
  val modeButtons: List<ColorModeButton>,
) : ViewState

data class ColorSliderChannelState(
  val name: String,
  val value: Float,
)

data class ColorModeButton(
  val name: String,
  val isSelected: Boolean,
)
