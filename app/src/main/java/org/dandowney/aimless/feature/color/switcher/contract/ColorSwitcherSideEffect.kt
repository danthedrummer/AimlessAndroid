package org.dandowney.aimless.feature.color.switcher.contract

import org.dandowney.aimless.library.architecture.ViewSideEffect

sealed interface ColorSwitcherSideEffect : ViewSideEffect {
  data object NavigateBack : ColorSwitcherSideEffect
}
