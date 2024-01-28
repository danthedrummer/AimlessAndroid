package org.dandowney.aimless.library.design.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import org.dandowney.aimless.library.design.header.Header
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun AimlessScreen(
  title: String?,
  modifier: Modifier = Modifier,
  leadingIcon: Painter? = null,
  onLeadingIconClick: () -> Unit = {},
  trailingIcon: Painter? = null,
  onTrailingIconClick: () -> Unit = {},
  content: @Composable () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = AimlessTheme.colors.backgroundPrimary),
  ) {
    Header(
      title = title,
      modifier = modifier,
      leadingIcon = leadingIcon,
      onLeadingIconClick = onLeadingIconClick,
      trailingIcon = trailingIcon,
      onTrailingIconClick = onTrailingIconClick,
    )

    content()
  }
}
