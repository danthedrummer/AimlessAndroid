package org.dandowney.aimless.feature.color.matcher.contract

import androidx.compose.ui.graphics.Color
import org.dandowney.aimless.library.architecture.ViewState

data class ColorMatcherState(
  val colorTarget: Color,
  val selectedColor: Color,
  val colorOptions: List<ColorOptionState>,
  val isResetting: Boolean,
) : ViewState

data class ColorOptionState(
  val channel: String,
  val color: Color,
  val isPressed: Boolean,
)
