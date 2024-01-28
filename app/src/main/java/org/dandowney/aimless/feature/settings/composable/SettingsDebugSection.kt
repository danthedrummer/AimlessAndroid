package org.dandowney.aimless.feature.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.dandowney.aimless.feature.settings.contract.SettingsState
import org.dandowney.aimless.library.design.text.ThemedText
import org.dandowney.aimless.library.design.theme.AimlessTheme

fun LazyListScope.settingsDebugSection(
  state: SettingsState,
) {
  item {
    ThemedText(
      text = "Debug",
      style = AimlessTheme.typography.h4,
    )
  }
  items(items = state.analytics) { data ->
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .shadow(
          elevation = 4.dp,
          shape = AimlessTheme.shapes.mediumRoundedCornerShape,
        )
        .background(
          color = AimlessTheme.colors.backgroundPrimary,
          shape = AimlessTheme.shapes.mediumRoundedCornerShape,
        )
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ThemedText(
        text = data.first,
        style = AimlessTheme.typography.body2,
      )

      ThemedText(
        text = data.second.toString(),
        style = AimlessTheme.typography.body2,
      )
    }
  }
}
