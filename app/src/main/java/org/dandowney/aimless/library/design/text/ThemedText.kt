package org.dandowney.aimless.library.design.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun ThemedText(
  text: String,
  modifier: Modifier = Modifier,
  style: TextStyle = AimlessTypography.body1,
  color: Color? = null,
) {
  Text(
    text = text,
    style = style.copy(
      color = color ?: style.color,
    ),
    modifier = modifier
  )
}
