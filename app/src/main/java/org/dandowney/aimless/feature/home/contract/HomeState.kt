package org.dandowney.aimless.feature.home.contract

import org.dandowney.aimless.library.architecture.ViewState

data class HomeState(
  val options: List<ActivityOption>,
) : ViewState

data class ActivityOption(
  val id: String,
  val title: String,
  val description: String,
  val tags: List<String>,
  val route: String,
)
