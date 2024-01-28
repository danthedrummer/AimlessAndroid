package org.dandowney.aimless.library.design.haptic

import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat

private const val VIBRATION_DURATION = 500L

@Suppress("DEPRECATION")
fun Context.playVibration() {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    val manager = ContextCompat.getSystemService(this, VibratorManager::class.java) as VibratorManager
    manager.vibrate(
      CombinedVibration.createParallel(
        VibrationEffect.createOneShot(VIBRATION_DURATION, 200)
      )
    )
  } else {
    val manager = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    manager.vibrate(VIBRATION_DURATION)
  }
}
