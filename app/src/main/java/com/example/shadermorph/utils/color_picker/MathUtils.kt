package com.example.shadermorph.utils.color_picker

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal enum class BoundaryCondition {
    Edge,
    Inside,
    Outside
}

internal object MathUtils {
    fun calculateAngle(x: Float, y: Float, radius: Float): Float {
        return ((atan2(y - radius, x - radius).toDegrees() + 360) % 360)
    }

    fun calculateDistance(x: Float, y: Float, center: Float): Float {
        return sqrt((x - center) * (x - center) + (y - center) * (y - center))
    }

    fun calculateBoundedPoint(
        x: Float,
        y: Float,
        length: Float,
        radius: Float,
        strategy: BoundaryCondition
    ): Offset {
        val distance = when (strategy) {
            BoundaryCondition.Edge -> radius
            BoundaryCondition.Inside -> length.coerceAtMost(radius)
            BoundaryCondition.Outside -> length.coerceAtLeast(radius)
        }
        return Offset(
            x + (distance - length) * cos(atan2(y - radius, x - radius)),
            y + (distance - length) * sin(atan2(y - radius, x - radius))
        )
    }

    private fun Float.toDegrees(): Float = Math.toDegrees(this.toDouble()).toFloat()
}