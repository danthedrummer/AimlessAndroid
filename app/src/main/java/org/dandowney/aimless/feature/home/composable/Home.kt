package org.dandowney.aimless.feature.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.dandowney.aimless.Screen
import org.dandowney.aimless.feature.home.HomeViewModel
import org.dandowney.aimless.feature.home.contract.HomeEvent
import org.dandowney.aimless.feature.home.contract.HomeState
import org.dandowney.aimless.feature.home.contract.ActivityOption
import org.dandowney.aimless.library.design.canvas.tiledBackground
import org.dandowney.aimless.library.design.header.Header
import org.dandowney.aimless.library.design.modifier.aimlessClickable
import org.dandowney.aimless.library.design.text.ThemedText
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun Home(
  viewModel: HomeViewModel = hiltViewModel(),
  onNavigationClicked: (String) -> Unit,
) {
  val state by viewModel.state
  Home(
    state = state,
    onNavigationClicked = onNavigationClicked,
    onActivitySelected = { viewModel.sendEvent(HomeEvent.ActivitySelected(it)) },
    onAimlessIconClicked = { viewModel.sendEvent(HomeEvent.AimlessIconClicked) }
  )
}

@Composable
private fun Home(
  state: HomeState,
  onNavigationClicked: (String) -> Unit,
  onActivitySelected: (ActivityOption) -> Unit,
  onAimlessIconClicked: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        color = AimlessTheme.colors.backgroundPrimary,
      )
      .drawBehind { tiledBackground() },
  ) {

    Header(
      trailingIcon = AimlessTheme.icons.settings,
      onTrailingIconClick = { onNavigationClicked(Screen.Settings.route) },
      onTitleClick = onAimlessIconClicked,
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = spacedBy(16.dp),
    ) {
      items(items = state.options) { aimless ->
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
              elevation = 4.dp,
              shape = AimlessTheme.shapes.largeRoundedCornerShape,
            )
            .background(
              color = AimlessTheme.colors.backgroundPrimary,
              shape = AimlessTheme.shapes.largeRoundedCornerShape,
            )
            .padding(16.dp)
            .aimlessClickable {
              onActivitySelected(aimless)
              onNavigationClicked(aimless.route)
            },
          horizontalAlignment = Alignment.Start,
        ) {
          ThemedText(
            text = aimless.title,
            style = AimlessTheme.typography.h5,
          )

          ThemedText(
            text = aimless.description,
            style = AimlessTheme.typography.subtitle1.copy(
              color = AimlessTheme.colors.textPlaceholder,
            ),
          )

          LazyRow(
            horizontalArrangement = spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp),
          ) {
            items(items = aimless.tags) { tag ->
              TagBadge(text = tag)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun TagBadge(
  text: String,
) {
  Box(
    modifier = Modifier
      .background(
        color = AimlessTheme.colors.backgroundButton,
        shape = AimlessTheme.shapes.mediumRoundedCornerShape,
      )
      .padding(8.dp),
    contentAlignment = Alignment.Center,
  ) {
    ThemedText(
      text = text,
      style = AimlessTheme.typography.overline.copy(
        color = AimlessTheme.colors.textButton,
      ),
    )
  }
}
