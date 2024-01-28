package org.dandowney.aimless.library.design.header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.dandowney.aimless.library.design.color.isDark
import org.dandowney.aimless.library.design.color.random
import org.dandowney.aimless.library.design.modifier.aimlessClickable
import org.dandowney.aimless.library.design.theme.AimlessTheme

@Composable
fun Header(
  modifier: Modifier = Modifier,
  title: String? = null,
  onTitleClick: () -> Unit = {},
  leadingIcon: Painter? = null,
  onLeadingIconClick: () -> Unit = {},
  trailingIcon: Painter? = null,
  onTrailingIconClick: () -> Unit = {},
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(64.dp)
      .shadow(elevation = 4.dp)
      .background(color = AimlessTheme.colors.backgroundPrimary)
      .padding(horizontal = 16.dp),
  ) {
    leadingIcon?.let { leadingIcon ->
      Icon(
        painter = leadingIcon,
        contentDescription = "Arrow for navigating back to the previous screen",
        tint = AimlessTheme.colors.iconPrimary,
        modifier = Modifier
          .align(Alignment.CenterStart)
          .size(24.dp)
          .aimlessClickable(onClick = onLeadingIconClick),
      )
    }

    if (title != null) {
      Text(
        text = title,
        style = AimlessTheme.typography.h5,
        modifier = Modifier.align(Alignment.Center)
          .clickable(onClick = onTitleClick),
      )
    } else {
      val iconColor = remember { mutableStateOf<Color?>(null) }
      val darkBackground = AimlessTheme.colors.backgroundPrimary.isDark()
      Icon(
        painter = AimlessTheme.icons.aimless,
        tint = iconColor.value ?: AimlessTheme.colors.iconPrimary,
        contentDescription = "Application icon",
        modifier = Modifier
          .align(Alignment.Center)
          .aimlessClickable {
            onTitleClick()
            var color: Color
            do {
              color = Color.random()
            } while ((darkBackground && color.isDark()) || (!darkBackground && !color.isDark()))
            iconColor.value = color
          },
      )
      LaunchedEffect(key1 = iconColor.value) {
        delay(1_000)
        iconColor.value = null
      }
    }

    trailingIcon?.let { trailingIcon ->
      Icon(
        painter = trailingIcon,
        contentDescription = "Icon for marking a aimless as a favourite",
        tint = AimlessTheme.colors.iconPrimary,
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .size(24.dp)
          .aimlessClickable(onClick = onTrailingIconClick),
      )
    }
  }
}
