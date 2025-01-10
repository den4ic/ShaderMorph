package com.example.shadermorph.presentation.morph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MorphShaderView(
    modifier: Modifier = Modifier,
    sliderValue: Float,
    firstSelectedShape: Int,
    secondSelectedShape: Int,
    isColorMode: Boolean,
    internalColor: FloatArray,
    externalColor: FloatArray,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MorphGLSurfaceView(context).apply {
                updateShaderValue(
                    sliderValue,
                    firstSelectedShape,
                    secondSelectedShape,
                    isColorMode,
                    internalColor,
                    externalColor
                )
            }
        },
        update = { view ->
            view.updateShaderValue(
                sliderValue,
                firstSelectedShape,
                secondSelectedShape,
                isColorMode,
                internalColor,
                externalColor
            )
        }
    )
}