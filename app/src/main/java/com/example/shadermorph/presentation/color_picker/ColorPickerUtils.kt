package com.example.shadermorph.presentation.color_picker

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.roundToInt

internal object ColorPickerUtils {
    fun calculateHueTransitionProgress(progress: Double): Pair<Double, HueTransition> {
        val hueIndex = (progress * 6).toInt()
        val hueTransition = when (hueIndex) {
            0 -> HueTransition.RedToYellow
            1 -> HueTransition.YellowToGreen
            2 -> HueTransition.GreenToCyan
            3 -> HueTransition.CyanToBlue
            4 -> HueTransition.BlueToPurple
            else -> HueTransition.PurpleToRed
        }
        return (progress * 6 - hueIndex) to hueTransition
    }
}

internal fun DrawScope.drawColorIndicator(color: Color, position: Offset) {
    drawCircle(color, radius = 30f, center = position)
    drawCircle(Color.White, radius = 30f, center = position, style = Stroke(5f))
    drawCircle(Color.LightGray, radius = 30f, center = position, style = Stroke(1f))
}

internal fun generateColor(transition: HueTransition, progress: Double, radiusProgress: Float, isLightCenter: Boolean): Color {
    val colorComponents = when (transition) {
        HueTransition.RedToYellow -> arrayOf(255, (255 * progress).toInt(), 0)
        HueTransition.YellowToGreen -> arrayOf((255 * (1 - progress)).toInt(), 255, 0)
        HueTransition.GreenToCyan -> arrayOf(0, 255, (255 * progress).toInt())
        HueTransition.CyanToBlue -> arrayOf(0, (255 * (1 - progress)).toInt(), 255)
        HueTransition.BlueToPurple -> arrayOf((255 * progress).toInt(), 0, 255)
        HueTransition.PurpleToRed -> arrayOf(255, 0, (255 * (1 - progress)).toInt())
    }

    return Color(
        red = colorComponents[0].adjustColorTo(isLightCenter, radiusProgress),
        green = colorComponents[1].adjustColorTo(isLightCenter, radiusProgress),
        blue = colorComponents[2].adjustColorTo(isLightCenter, radiusProgress)
    )
}

private fun Int.adjustColorTo(toWhite: Boolean, progress: Float): Int {
    val target = if (toWhite) 255f else 0f
    return ((this.toFloat() + (target - this.toFloat()) * progress).roundToInt())
}