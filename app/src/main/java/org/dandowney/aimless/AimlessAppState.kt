package org.dandowney.aimless

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {

  data object Home : Screen("home")
  data object Settings : Screen("settings")

  sealed class Color(route: String) : Screen("color/$route") {
    data object Switcher : Color("switcher")
    data object Slider : Color("slider/{mode}")
    data object Matcher : Color("matcher")
    data object Filters : Color("filters")
  }

  sealed class Tactile(route: String) : Screen("tactile/$route") {
    data object Drag : Tactile("drag")
    data object Clickers : Tactile("clickers")
  }
}

@Composable
fun rememberAppState(
  navHostController: NavHostController = rememberNavController(),
): AimlessAppState = remember {
  AimlessAppState(navHostController = navHostController)
}

class AimlessAppState(
  val navHostController: NavHostController,
) {

  fun navigateBack() {
    navHostController.popBackStack()
  }

  fun navigateTo(screen: String) {
    navHostController.navigate(route = screen)
  }
}
