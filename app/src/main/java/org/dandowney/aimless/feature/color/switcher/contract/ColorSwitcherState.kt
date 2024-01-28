package org.dandowney.aimless.feature.color.switcher.contract

import androidx.compose.ui.graphics.Color
import org.dandowney.aimless.library.architecture.ViewState

data class ColorSwitcherState(
  val availableColors: List<Color>,
  val selectedColor: Color?,
) : ViewState
