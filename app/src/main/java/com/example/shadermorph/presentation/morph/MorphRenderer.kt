package com.example.shadermorph.presentation.morph

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.shadermorph.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MorphRenderer(
    private val context: Context,
) : GLSurfaceView.Renderer {
    private var shaderProgram: Int = 0
    private var screenWidth = 0
    private var screenHeight = 0
    private val vertices = floatArrayOf(
        -1f, -1f, 0f,
        1f, -1f, 0f,
        -1f,  1f, 0f,
        1f,  1f, 0f
    )
    private var vertexBuffer: FloatBuffer? = null
    private var sliderValue: Float = 0.0f

    private var firstSelectedShape: Int = 0
    private var secondSelectedShape: Int = 0

    private var isColorMode: Boolean = true

    private var internalColor: FloatArray = floatArrayOf(0.1f, 0.8f, 1.0f)
    private var externalColor: FloatArray = floatArrayOf(0.9f, 0.6f, 0.3f)

    init {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
        vertexBuffer?.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vertexShaderCode = loadShaderFromRawResource(context, R.raw.vertex_shader)
        val fragmentShaderCode = loadShaderFromRawResource(context, R.raw.morph_fragment_shader)

        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        shaderProgram = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertexShader)
            GLES30.glAttachShader(it, fragmentShader)
            GLES30.glLinkProgram(it)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUseProgram(shaderProgram)

        GLES30.glUniform2f(
            GLES30.glGetUniformLocation(shaderProgram, "iResolution"),
            screenWidth.toFloat(), screenHeight.toFloat()
        )
        GLES30.glUniform1f(
            GLES30.glGetUniformLocation(shaderProgram, "morphFactor"),
            sliderValue
        )
        GLES30.glUniform1i(
            GLES30.glGetUniformLocation(shaderProgram, "firstShapeIndex"),
            firstSelectedShape
        )
        GLES30.glUniform1i(
            GLES30.glGetUniformLocation(shaderProgram, "secondShapeIndex"),
            secondSelectedShape
        )
        GLES30.glUniform1i(
            GLES30.glGetUniformLocation(shaderProgram, "isColorMode"),
            if (isColorMode) 1 else 0
        )

        GLES30.glUniform3f(
            GLES30.glGetUniformLocation(shaderProgram, "internalColor"),
            internalColor[0], internalColor[1], internalColor[2]
        )
        GLES30.glUniform3f(
            GLES30.glGetUniformLocation(shaderProgram, "externalColor"),
            externalColor[0], externalColor[1], externalColor[2]
        )

        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 12, vertexBuffer)
        GLES30.glEnableVertexAttribArray(0)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
    }

    fun updateSliderValue(sliderValue: Float) {
        this.sliderValue = sliderValue
    }

    fun updateSelectedShape(firstSelectedShape: Int, secondSelectedShape: Int) {
        this.firstSelectedShape = firstSelectedShape
        this.secondSelectedShape = secondSelectedShape
    }

    fun updateColorMode(isColorMode: Boolean) {
        this.isColorMode = isColorMode
    }

    fun updateColors(internalColor: FloatArray, externalColor: FloatArray) {
        this.internalColor = internalColor
        this.externalColor = externalColor
    }

    private fun loadShaderFromRawResource(context: Context, resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES30.glCreateShader(type).also { shader ->
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }
}