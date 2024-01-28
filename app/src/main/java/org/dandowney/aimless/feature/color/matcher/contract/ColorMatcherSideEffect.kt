package org.dandowney.aimless.feature.color.matcher.contract

import org.dandowney.aimless.library.architecture.ViewSideEffect

sealed interface ColorMatcherSideEffect : ViewSideEffect {
  data object NavigateBack : ColorMatcherSideEffect
  data object PlayVibration : ColorMatcherSideEffect
}
