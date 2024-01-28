package org.dandowney.aimless.library.design.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

class AimlessShapes {

  val smallRoundedCornerShape: Shape
    @Composable
    get() = RoundedCornerShape(4.dp)

  val mediumRoundedCornerShape: Shape
    @Composable
    get() = RoundedCornerShape(8.dp)

  val largeRoundedCornerShape: Shape
    @Composable
    get() = RoundedCornerShape(12.dp)
}

internal val LocalShapes = staticCompositionLocalOf { AimlessShapes() }
