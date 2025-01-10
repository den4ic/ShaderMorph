package com.example.shadermorph.presentation.morph

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MorphGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {
    private val renderer: MorphRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = MorphRenderer(context)
        setRenderer(renderer)
    }

    fun updateShaderValue(
        sliderValue: Float,
        firstSelectedShape: Int,
        secondSelectedShape: Int,
        isColorMode: Boolean,
        internalColor: FloatArray,
        externalColor: FloatArray
    ) {
        renderer.updateSliderValue(sliderValue)
        renderer.updateSelectedShape(firstSelectedShape, secondSelectedShape)
        renderer.updateColorMode(isColorMode)
        renderer.updateColors(internalColor, externalColor)
    }
}