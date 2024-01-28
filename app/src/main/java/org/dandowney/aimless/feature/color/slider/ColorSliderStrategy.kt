package org.dandowney.aimless.feature.color.slider

import androidx.compose.ui.graphics.Color
import org.dandowney.aimless.feature.color.slider.contract.ColorSliderChannelState

interface ColorSliderStrategy {

  val channels: List<ColorSliderChannelState>

  fun setColor(color: Color)

  fun updateChannel(channel: ColorSliderChannelState, newValue: Float)

  fun getColor(): Color

  companion object {
    fun from(mode: String): ColorSliderStrategy = when (mode) {
      "rgb" -> RgbColorSliderStrategy()
      "hsv" -> HsvColorSliderStrategy()
      "cmyk" -> CmykColorSliderStrategy()
      else -> error("Invalid color mode selected -> $mode")
    }
  }
}

class RgbColorSliderStrategy : ColorSliderStrategy {

  override var channels: List<ColorSliderChannelState> = emptyList()

  override fun setColor(color: Color) {
    channels = listOf(
      ColorSliderChannelState(name = "Red", color.red),
      ColorSliderChannelState(name = "Green", color.green),
      ColorSliderChannelState(name = "Blue", color.blue),
    )
  }

  override fun updateChannel(channel: ColorSliderChannelState, newValue: Float) {
    channels = channels.map {
      if (it == channel) {
        it.copy(value = newValue)
      } else {
        it
      }
    }
  }

  override fun getColor(): Color = Color(
    red = channels[0].value,
    green = channels[1].value,
    blue = channels[2].value,
  )

}

class HsvColorSliderStrategy : ColorSliderStrategy {

  override var channels: List<ColorSliderChannelState> = emptyList()

  override fun setColor(color: Color) {
    val hsv = FloatArray(3)
    // Abusing the legacy Android color system to convert to HSV because I'm too lazy
    // to implement the maths myself and I don't want to use a library
    android.graphics.Color.RGBToHSV(
      (255 * color.red).toInt(),
      (255 * color.green).toInt(),
      (255 * color.blue).toInt(),
      hsv,
    )
    channels = listOf(
      ColorSliderChannelState("Hue", hsv[0] / 360),
      ColorSliderChannelState("Saturation", hsv[1]),
      ColorSliderChannelState("Value", hsv[2]),
    )
  }

  override fun updateChannel(channel: ColorSliderChannelState, newValue: Float) {
    channels = channels.map {
      if (it == channel) {
        it.copy(value = newValue)
      } else {
        it
      }
    }
  }

  override fun getColor(): Color = Color.hsv(
    hue = channels[0].value * 360,
    saturation = channels[1].value,
    value = channels[2].value,
  )

}

class CmykColorSliderStrategy : ColorSliderStrategy {

  override var channels: List<ColorSliderChannelState> = emptyList()

  override fun setColor(color: Color) {
    val cmyk = color.toCmyk()
    channels = listOf(
      ColorSliderChannelState(name = "Cyan", value = cmyk.cyan),
      ColorSliderChannelState(name = "Magenta", value = cmyk.magenta),
      ColorSliderChannelState(name = "Yellow", value = cmyk.yellow),
      ColorSliderChannelState(name = "Key", value = cmyk.key),
    )
  }

  override fun updateChannel(channel: ColorSliderChannelState, newValue: Float) {
    channels = channels.map {
      if (it == channel) it.copy(value = newValue) else it
    }
  }

  override fun getColor(): Color {
    return Cmyk(
      cyan = channels[0].value,
      magenta = channels[1].value,
      yellow = channels[2].value,
      key = channels[3].value,
    ).toColor()
  }

  private fun Color.toCmyk(): Cmyk {
    val key = 1 - maxOf(red, green, blue)
    return Cmyk(
      key = key,
      cyan = (1 - red - key) / (1 - key),
      magenta = (1 - green - key) / (1 - key),
      yellow = (1 - blue - key) / (1 - key),
    )
  }

  private fun Cmyk.toColor(): Color {
    return Color(
      red = (1 - cyan) * (1 - key),
      green = (1 - magenta) * (1 - key),
      blue = (1 - yellow) * (1 - key),
    )
  }

  data class Cmyk(
    val cyan: Float,
    val magenta: Float,
    val yellow: Float,
    val key: Float,
  )
}
