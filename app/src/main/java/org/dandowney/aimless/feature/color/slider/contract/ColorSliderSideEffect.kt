package org.dandowney.aimless.feature.color.slider.contract

import org.dandowney.aimless.library.architecture.ViewSideEffect

sealed interface ColorSliderSideEffect : ViewSideEffect {
  data object NavigateBack : ColorSliderSideEffect
}
