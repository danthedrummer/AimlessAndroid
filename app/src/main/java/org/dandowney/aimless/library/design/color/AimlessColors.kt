package org.dandowney.aimless.library.design.color

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color

class AimlessColors(
  // surfaces
  backgroundPrimary: Color,
  backgroundSecondary: Color,
  backgroundButton: Color,

  // typography
  textPrimary: Color,
  textPlaceholder: Color,
  textButton: Color,

  // icons
  iconPrimary: Color,
  iconSecondary: Color,
) {

  var backgroundPrimary by mutableStateOf(backgroundPrimary, structuralEqualityPolicy())
    private set
  var backgroundSecondary by mutableStateOf(backgroundSecondary, structuralEqualityPolicy())
    private set
  var backgroundButton by mutableStateOf(backgroundButton, structuralEqualityPolicy())
    private set
  var textPrimary by mutableStateOf(textPrimary, structuralEqualityPolicy())
    private set
  var textPlaceholder by mutableStateOf(textPlaceholder, structuralEqualityPolicy())
    private set
  var textButton by mutableStateOf(textButton, structuralEqualityPolicy())
    private set
  var iconPrimary by mutableStateOf(iconPrimary, structuralEqualityPolicy())
    private set
  var iconSecondary by mutableStateOf(iconSecondary, structuralEqualityPolicy())
    private set


  fun copy(
    backgroundPrimary: Color = this.backgroundPrimary,
    backgroundSecondary: Color = this.backgroundSecondary,
    backgroundButton: Color = this.backgroundButton,
    textPrimary: Color = this.textPrimary,
    textPlaceholder: Color = this.textPlaceholder,
    textButton: Color = this.textButton,
    iconPrimary: Color = this.iconPrimary,
    iconSecondary: Color = this.iconSecondary,
  ): AimlessColors = AimlessColors(
    backgroundPrimary = backgroundPrimary,
    backgroundSecondary = backgroundSecondary,
    backgroundButton = backgroundButton,
    textPrimary = textPrimary,
    textPlaceholder = textPlaceholder,
    textButton = textButton,
    iconPrimary = iconPrimary,
    iconSecondary = iconSecondary,
  )

  fun updateColorsFrom(other: AimlessColors) {
    this.backgroundPrimary = other.backgroundPrimary
    this.backgroundSecondary = other.backgroundSecondary
    this.backgroundButton = other.backgroundButton
    this.textPrimary = other.textPrimary
    this.textPlaceholder = other.textPlaceholder
    this.textButton = other.textButton
    this.iconPrimary = other.iconPrimary
    this.iconSecondary = other.iconSecondary
  }
}

internal val DarkColorPalette
  get() = AimlessColors(
    backgroundPrimary = Gray800,
    backgroundSecondary = Gray600,
    backgroundButton = Gray200,
    textPrimary = Gray100,
    textPlaceholder = Gray200,
    textButton = Gray800,
    iconPrimary = Gray100,
    iconSecondary = Gray300,
  )

internal val LightColorPalette
  get() = AimlessColors(
    backgroundPrimary = Gray200,
    backgroundSecondary = Gray300,
    backgroundButton = Gray800,
    textPrimary = Gray800,
    textPlaceholder = Gray700,
    textButton = Gray200,
    iconPrimary = Gray800,
    iconSecondary = Gray600,
  )

internal val LocalColors = staticCompositionLocalOf { LightColorPalette }
