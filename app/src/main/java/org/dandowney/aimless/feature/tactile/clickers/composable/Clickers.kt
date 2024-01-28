package org.dandowney.aimless.feature.tactile.clickers.composable

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import org.dandowney.aimless.feature.tactile.clickers.ClickersViewModel
import org.dandowney.aimless.feature.tactile.clickers.contract.ClickersEvent
import org.dandowney.aimless.feature.tactile.clickers.contract.ClickersSideEffect
import org.dandowney.aimless.feature.tactile.clickers.contract.ClickersState
import org.dandowney.aimless.library.design.container.AimlessScreen
import org.dandowney.aimless.library.design.modifier.reactiveShadow
import org.dandowney.aimless.library.design.modifier.aimlessClickable
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun Clickers(
  viewModel: ClickersViewModel = hiltViewModel(),
  onNavigateBack: () -> Unit,
) {
  LaunchedEffect(key1 = Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ClickersSideEffect.NavigateBack -> onNavigateBack()
      }
    }
  }
  BackHandler {
    viewModel.sendEvent(ClickersEvent.BackClicked)
  }
  val state by viewModel.state
  Clickers(
    state = state,
    onBackClick = { viewModel.sendEvent(ClickersEvent.BackClicked) },
    onClickerPressed = { viewModel.sendEvent(ClickersEvent.ClickedPressed(it)) },
    onClickerUnpressed = { viewModel.sendEvent(ClickersEvent.ClickerUnpressed(it)) },
  )
}

@Composable
private fun Clickers(
  state: ClickersState,
  onBackClick: () -> Unit,
  onClickerPressed: (Int) -> Unit,
  onClickerUnpressed: (Int) -> Unit,
) {
  AimlessScreen(
    title = "Clickers",
    leadingIcon = AimlessTheme.icons.back,
    onLeadingIconClick = onBackClick,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(),
        contentPadding = PaddingValues(32.dp),
        verticalArrangement = spacedBy(32.dp),
        horizontalArrangement = spacedBy(32.dp),
      ) {
        items(items = state.clickers) { clicker ->
          val elevation by animateDpAsState(
            targetValue = if (clicker.isPressed) 0.dp else 12.dp,
            label = "",
          )
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(1F)
              .reactiveShadow(
                elevation = elevation,
                shape = AimlessTheme.shapes.largeRoundedCornerShape,
                surfaceColor = AimlessTheme.colors.backgroundPrimary,
              )
//              .shadow(
//                elevation = elevation,
//                shape = AimlessTheme.shapes.largeRoundedCornerShape,
//                spotColor = AimlessTheme.colors.iconPrimary,
//                ambientColor = AimlessTheme.colors.iconPrimary,
//              )
              .background(
                color = AimlessTheme.colors.backgroundPrimary,
                shape = AimlessTheme.shapes.largeRoundedCornerShape,
              )
              .aimlessClickable {
                onClickerPressed(clicker.id)
              },
          )
          LaunchedEffect(key1 = clicker) {
            if (clicker.isPressed) {
              delay(state.resetTimer)
              onClickerUnpressed(clicker.id)
            }
          }
        }
      }
    }
  }
}
