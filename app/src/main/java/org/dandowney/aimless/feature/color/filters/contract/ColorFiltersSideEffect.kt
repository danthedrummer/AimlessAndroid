package org.dandowney.aimless.feature.color.filters.contract

import org.dandowney.aimless.library.architecture.ViewSideEffect

sealed interface ColorFiltersSideEffect : ViewSideEffect {
  data object NavigateBack : ColorFiltersSideEffect
}
