package org.dandowney.aimless.library.design.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import org.dandowney.aimless.library.design.color.Gray600

fun DrawScope.tiledBackground() {
  val tileSize = 80F
  var widthOffset = 0F
  var heightOffset = 0F
  var counter = 0
  var row = 0
  val alpha = 0.2F
  val color = Gray600
  while (heightOffset < size.height) {
    while (widthOffset < size.width) {
      when (counter % 3) {
        0 -> drawTriangle(
          topLeftOffset = Offset(widthOffset + (tileSize / 4), heightOffset + (tileSize / 4)),
          size = Size(tileSize / 2, tileSize / 2),
          color = color,
          alpha = alpha,
        )

        1 -> drawRoundRect(
          topLeft = Offset(widthOffset + (tileSize / 4), heightOffset + (tileSize / 4)),
          size = Size(tileSize / 2, tileSize / 2),
          color = color,
          alpha = alpha,
          style = Stroke(2F),
        )

        2 -> drawCircle(
          center = Offset(widthOffset + (tileSize / 2), heightOffset + (tileSize / 2)),
          radius = tileSize / 3,
          color = color,
          alpha = alpha,
          style = Stroke(2F),
        )
      }

      widthOffset += tileSize
      counter++
    }
    heightOffset += tileSize
    row++
    counter = 0
    widthOffset = -(tileSize / 3) * row
  }
}

fun DrawScope.drawTriangle(
  topLeftOffset: Offset,
  size: Size,
  color: Color,
  alpha: Float,
) {
  val path = Path()
  path.moveTo(topLeftOffset.x + (size.width / 2), topLeftOffset.y)
  path.lineTo(topLeftOffset.x + size.width, topLeftOffset.y + size.height)
  path.lineTo(topLeftOffset.x, topLeftOffset.y + size.height)
  path.lineTo(topLeftOffset.x + (size.width / 2), topLeftOffset.y)
  drawPath(path = path, color = color, style = Stroke(width = 2F), alpha = alpha)
}
