package org.dandowney.aimless.feature.home

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import org.dandowney.aimless.Screen
import org.dandowney.aimless.feature.home.contract.HomeEvent
import org.dandowney.aimless.feature.home.contract.HomeSideEffect
import org.dandowney.aimless.feature.home.contract.HomeState
import org.dandowney.aimless.feature.home.contract.ActivityOption
import org.dandowney.aimless.library.architecture.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val firebaseAnalytics: FirebaseAnalytics,
) : BaseViewModel<HomeEvent, HomeState, HomeSideEffect>() {

  override fun createInitialState(): HomeState {
    return HomeState(
      options = listOf(
        ActivityOption(
          id = "color_switcher",
          title = "Colour Switcher",
          description = "An animating colour changer",
          tags = listOf("colour", "animation", "buttons"),
          route = Screen.Color.Switcher.route,
        ),
        ActivityOption(
          id = "color_mixer",
          title = "Colour Mixer",
          description = "Mix red, green, and blue to make your own colour",
          tags = listOf("colour"),
          route = Screen.Color.Slider.route.replace("{mode}", "rgb"),
        ),
        ActivityOption(
          id = "shape_slot",
          title = "Shape Slot",
          description = "Guide the box to slot neatly into its home",
          tags = listOf("gesture", "haptic", "shape"),
          route = Screen.Tactile.Drag.route,
        ),
        ActivityOption(
          id = "clickers",
          title = "Clickers",
          description = "A group of buttons that don't want to be pressed",
          tags = listOf("tactile", "buttons"),
          route = Screen.Tactile.Clickers.route,
        ),
        ActivityOption(
          id = "color_matcher",
          title = "Colour Matcher",
          description = "Find the combination to match the target colour",
          tags = listOf("colour", "buttons", "puzzle"),
          route = Screen.Color.Matcher.route,
        ),
        ActivityOption(
          id = "color_filters",
          title = "Colour Filters",
          description = "Watch the filters argue about which colour to become",
          tags = listOf("colour", "motion"),
          route = Screen.Color.Filters.route,
        ),
      ),
    )
  }

  override suspend fun handleEvent(event: HomeEvent) {
    when (event) {
      is HomeEvent.ActivitySelected -> aimlessOpened(event.aimlessOption)
      is HomeEvent.AimlessIconClicked -> aimlessIconClicked()
    }
  }

  private fun aimlessOpened(aimlessOption: ActivityOption) {
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
      param(FirebaseAnalytics.Param.ITEM_ID, aimlessOption.id)
      param(FirebaseAnalytics.Param.ITEM_NAME, aimlessOption.title)
    }
  }

  private fun aimlessIconClicked() {
    firebaseAnalytics.logEvent("aimless_home_icon_clicked") {}
  }
}
