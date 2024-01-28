package org.dandowney.aimless.feature.tactile.clickers.contract

import org.dandowney.aimless.library.architecture.ViewState

data class ClickersState(
  val resetTimer: Long,
  val clickers: List<ClickerItemState>,
) : ViewState

data class ClickerItemState(
  val id: Int,
  val isPressed: Boolean,
)
