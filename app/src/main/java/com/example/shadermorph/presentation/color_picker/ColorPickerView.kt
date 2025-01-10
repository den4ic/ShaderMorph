package com.example.shadermorph.presentation.color_picker

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.shadermorph.utils.color_picker.MathUtils.calculateAngle
import com.example.shadermorph.utils.color_picker.MathUtils.calculateBoundedPoint
import com.example.shadermorph.utils.color_picker.MathUtils.calculateDistance
import com.example.shadermorph.ui.theme.color_picker.ColorPalette
import com.example.shadermorph.utils.color_picker.BoundaryCondition

@ExperimentalComposeUiApi
@Composable
internal fun ColorPickerView(
    modifier: Modifier = Modifier,
    isLightCenter: Boolean = true,
    onColorPicked: (Color) -> Unit
) {
    var circleRadius by remember { mutableStateOf(0f) }
    var pickerPosition by remember(circleRadius) { mutableStateOf(Offset(circleRadius, circleRadius)) }
    var currentColor by remember { mutableStateOf(if (isLightCenter) Color.White else Color.Black) }

    LaunchedEffect(currentColor) {
        onColorPicked(currentColor)
    }

    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        Canvas(
            modifier = modifier
                .size(200.dp)
                .onSizeChanged { circleRadius = it.width / 2f }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            val angle = calculateAngle(it.x, it.y, circleRadius)
                            val distance = calculateDistance(it.x, it.y, circleRadius)
                            val radiusProgress = 1 - (distance / circleRadius).coerceIn(0f, 1f)
                            val angleProgress = angle / 360f
                            val (transitionProgress, transition) = ColorPickerUtils.calculateHueTransitionProgress(
                                angleProgress.toDouble()
                            )
                            currentColor = generateColor(
                                transition,
                                transitionProgress,
                                radiusProgress,
                                isLightCenter
                            )
                            pickerPosition = calculateBoundedPoint(
                                it.x,
                                it.y,
                                distance,
                                circleRadius,
                                BoundaryCondition.Inside
                            )
                        }
                    }
                    true
                }
        ) {
            drawCircle(Brush.sweepGradient(ColorPalette.gradientColors))
            drawCircle(
                ShaderBrush(
                    RadialGradientShader(
                        Offset(size.width / 2f, size.height / 2f),
                        colors = listOf(if (isLightCenter) Color.White else Color.Black, Color.Transparent),
                        radius = size.width / 2f
                    )
                )
            )
            drawColorIndicator(currentColor, pickerPosition)
        }
    }
}