package com.example.shadermorph.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shadermorph.domain.model.Shape
import com.example.shadermorph.domain.usecase.GetAllShapeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import com.example.shadermorph.utils.Result
import javax.inject.Inject

// TODO
//  Сделать рефакторинг по архитектуре
//  Убрать все библиотеки лишние и импорты из проекта
//  Сделать скрины и залить на гитхаб оупенсурс с ридми
//  Выкатить статью по шейдерам хабр медиум реддит и еще был сайт 3 (signed distance field - поле расстояний со знаком) (Тема - Морфинг еффект через SDF (signed distance field) с использволанием шейдеров в Android)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val getAllShapeUseCase: GetAllShapeUseCase
) : ViewModel() {

    private val _listShapes: MutableStateFlow<List<Shape>> = MutableStateFlow(listOf())
    val listShapes: StateFlow<List<Shape>> get() = _listShapes

    private val _sliderValue = MutableStateFlow(0.0f)
    val sliderValue: StateFlow<Float> get() = _sliderValue

    private val _selectedShapes = MutableStateFlow(Pair(0, 16))
    val selectedShapes: StateFlow<Pair<Int, Int>> get() = _selectedShapes

    private val _morphTitleText = MutableStateFlow("Morph Circle into Star")
    val morphTitleText: StateFlow<String> get() = _morphTitleText

    private val _isColorMode = MutableStateFlow(true)
    val isColorMode: StateFlow<Boolean> get() = _isColorMode

    private val _colorsSelected = MutableStateFlow(floatArrayOf(0.1f, 0.8f, 1.0f) to floatArrayOf(0.9f, 0.6f, 0.3f))
    val colorsSelected: StateFlow<Pair<FloatArray, FloatArray>> get() = _colorsSelected

    init {
        loadShapes()
    }

    fun updateSliderValue(value: Float) {
        _sliderValue.value = value
    }

    fun updateSelectedShapes(firstIndex: Int, secondIndex: Int) {
        _selectedShapes.value = Pair(firstIndex, secondIndex)
    }

    fun updateMorphTitle(firstName: String?, secondName: String?) {
        _morphTitleText.value = "Morph $firstName into $secondName"
    }

    fun updateColorMode(isColorMode: Boolean) {
        _isColorMode.value = isColorMode
    }

    fun updateColorsSelected(colorsSelected: Pair<FloatArray, FloatArray>) {
        _colorsSelected.value = colorsSelected
    }

    private fun loadShapes() = viewModelScope.launch {
        getAllShapeUseCase()
            .flowOn(Dispatchers.IO)
            .collect { result ->
                when (result) {
                    is Result.Success -> {
                        _listShapes.value = result.value
                    }
                    is Result.Failure -> {
                        Log.e("EXAMPLE_TAG", "Error", result.throwable)
                    }
                }
            }
    }
}
