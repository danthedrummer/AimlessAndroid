package org.dandowney.aimless.library.design.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.random.Random.Default.nextInt

fun Color.isDark(): Boolean = luminance() < 0.5

fun Color.Companion.random(): Color = Color(
  red = nextInt(255),
  green = nextInt(255),
  blue = nextInt(255),
)
