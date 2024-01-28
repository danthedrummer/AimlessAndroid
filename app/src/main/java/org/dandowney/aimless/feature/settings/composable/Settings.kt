package org.dandowney.aimless.feature.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.dandowney.aimless.feature.settings.SettingsViewModel
import org.dandowney.aimless.feature.settings.contract.SettingsEvent
import org.dandowney.aimless.feature.settings.contract.SettingsSideEffect
import org.dandowney.aimless.feature.settings.contract.SettingsState
import org.dandowney.aimless.library.design.canvas.tiledBackground
import org.dandowney.aimless.library.design.header.Header
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun Settings(
  viewModel: SettingsViewModel = hiltViewModel(),
  onNavigateBack: () -> Unit,
) {
  val localUriHandler = LocalUriHandler.current
  LaunchedEffect(key1 = Unit) {
    this.launch {
      viewModel.effect.collect { effect ->
        when (effect) {
          is SettingsSideEffect.OpenPrivacyPolicy -> localUriHandler.openUri(effect.uri)
        }
      }
    }
    viewModel.initialize()
  }

  val state by viewModel.state
  Settings(
    state = state,
    onBackClicked = onNavigateBack,
    onDarkModeToggled = { viewModel.sendEvent(SettingsEvent.ToggleDarkTheme(it)) },
    onPrivacyPolicyClicked = { viewModel.sendEvent(SettingsEvent.PrivacyPolicyClicked) }
  )
}

@Composable
private fun Settings(
  state: SettingsState,
  onBackClicked: () -> Unit,
  onDarkModeToggled: (Boolean) -> Unit,
  onPrivacyPolicyClicked: () -> Unit,
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
      title = state.title,
      leadingIcon = AimlessTheme.icons.back,
      onLeadingIconClick = onBackClicked,
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize(),
      verticalArrangement = spacedBy(16.dp),
      contentPadding = PaddingValues(16.dp)
    ) {
      settingsAboutSection(
        onPrivacyPolicyClicked = onPrivacyPolicyClicked,
      )

      settingsAppearanceSection(
        state = state,
        onDarkModeToggled = onDarkModeToggled,
      )

      if (state.isDebug) {
        settingsDebugSection(state = state)
      }
    }
  }
}
