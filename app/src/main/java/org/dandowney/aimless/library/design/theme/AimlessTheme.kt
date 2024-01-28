package org.dandowney.aimless.library.design.theme


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import org.dandowney.aimless.library.design.color.DarkColorPalette
import org.dandowney.aimless.library.design.color.LightColorPalette
import org.dandowney.aimless.library.design.color.LocalColors
import org.dandowney.aimless.library.design.color.AimlessColors
import org.dandowney.aimless.library.design.icon.LocalIcons
import org.dandowney.aimless.library.design.icon.AimlessIcons
import org.dandowney.aimless.library.design.shape.LocalShapes
import org.dandowney.aimless.library.design.shape.AimlessShapes
import org.dandowney.aimless.library.design.text.LocalTypography
import org.dandowney.aimless.library.design.text.AimlessTypography
import org.dandowney.aimless.library.design.theme.AimlessTheme.icons
import org.dandowney.aimless.library.design.theme.AimlessTheme.shapes
import org.dandowney.aimless.library.design.theme.AimlessTheme.typography

@Composable
fun AimlessTheme(
  themeViewModel: ThemeViewModel = hiltViewModel(),
  content: @Composable () -> Unit,
) {
  val darkTheme by themeViewModel.isDarkTheme.collectAsState(initial = isDarkTheme())
  AimlessTheme(
    darkTheme = darkTheme,
    content = content,
  )
}

@Composable
fun AimlessTheme(
  darkTheme: Boolean,
  content: @Composable () -> Unit,
) {
  val colors = if (darkTheme) DarkColorPalette else LightColorPalette

  val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }

  LocalDarkTheme.current.value = darkTheme

  CompositionLocalProvider(
    LocalColors provides rememberedColors,
    LocalShapes provides shapes,
    LocalIcons provides icons,
    LocalTypography provides typography,
    content = content,
  )
}

object AimlessTheme {

  val colors: AimlessColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

  val shapes: AimlessShapes
    @Composable
    @ReadOnlyComposable
    get() = LocalShapes.current

  val icons: AimlessIcons
    @Composable
    @ReadOnlyComposable
    get() = LocalIcons.current

  val typography: AimlessTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current

}

@Composable
@ReadOnlyComposable
fun isDarkTheme() = LocalDarkTheme.current.value

private val LocalDarkTheme = compositionLocalOf { mutableStateOf(false) }
