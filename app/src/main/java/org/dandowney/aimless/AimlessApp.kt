package org.dandowney.aimless

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.dandowney.aimless.feature.color.filters.composable.ColorFilters
import org.dandowney.aimless.feature.color.matcher.composable.ColorMatcher
import org.dandowney.aimless.feature.color.slider.composable.ColorSlider
import org.dandowney.aimless.feature.color.switcher.composable.ColorSwitcher
import org.dandowney.aimless.feature.home.composable.Home
import org.dandowney.aimless.feature.settings.composable.Settings
import org.dandowney.aimless.feature.tactile.clickers.composable.Clickers
import org.dandowney.aimless.feature.tactile.slot.composable.Slot

@Composable
fun AimlessApp(
  appState: AimlessAppState = rememberAppState(),
) {
  NavHost(
    navController = appState.navHostController,
    startDestination = Screen.Home.route,
    enterTransition = { EnterTransition.None },
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { ExitTransition.None },
  ) {

    composable(Screen.Home.route) {
      Home(onNavigationClicked = appState::navigateTo)
    }

    composable(Screen.Settings.route) {
      Settings(onNavigateBack = appState::navigateBack)
    }

    composable(Screen.Color.Switcher.route) {
      ColorSwitcher(onNavigateBack = appState::navigateBack)
    }

    composable(
      route = Screen.Color.Slider.route,
      arguments = listOf(navArgument(name = "mode") { type = NavType.StringType }),
    ) { backStackEntry ->
      val mode = backStackEntry.arguments?.getString("mode") ?: error("Color mode has not been given")
      ColorSlider(onNavigateBack = appState::navigateBack, mode = mode)
    }

    composable(Screen.Tactile.Drag.route) {
      Slot(onNavigateBack = appState::navigateBack)
    }

    composable(Screen.Tactile.Clickers.route) {
      Clickers(onNavigateBack = appState::navigateBack)
    }

    composable(Screen.Color.Matcher.route) {
      ColorMatcher(onNavigateBack = appState::navigateBack)
    }

    composable(Screen.Color.Filters.route) {
      ColorFilters(onNavigateBack = appState::navigateBack)
    }
  }
}
