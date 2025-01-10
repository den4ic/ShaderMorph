package com.example.shadermorph.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shadermorph.presentation.color_picker.ColorPickerBottomSheet
import com.example.shadermorph.presentation.morph.MorphShaderView
import com.example.shadermorph.ui.components.AcceptButton
import com.example.shadermorph.ui.components.ColorModeSwitch

@Composable
fun MainScreen(
    innerPadding: PaddingValues
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val morphText by mainViewModel.morphTitleText.collectAsState()
    val sliderValue by mainViewModel.sliderValue.collectAsState()
    val selectedShapes by mainViewModel.selectedShapes.collectAsState()
    val isColorMode by mainViewModel.isColorMode.collectAsState()
    val listShapes by mainViewModel.listShapes.collectAsState()
    val colorsSelected by mainViewModel.colorsSelected.collectAsState()

    var showShapeBottomSheet by remember { mutableStateOf(false) }
    var showColorPickerBottomSheet by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(
            modifier = Modifier
                .padding(innerPadding)
        )

        Text(
            text = morphText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.CenterHorizontally)
        )

        MorphShaderView(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .clip(RoundedCornerShape(16.dp)),
            sliderValue = sliderValue,
            firstSelectedShape = selectedShapes.first,
            secondSelectedShape = selectedShapes.second,
            isColorMode = isColorMode,
            internalColor = colorsSelected.first,
            externalColor = colorsSelected.second
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            text = "Morphing: ${"%.2f".format(sliderValue)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            value = sliderValue,
            onValueChange = { mainViewModel.updateSliderValue(it) },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
            )
        )

        Row {
            ColorModeSwitch(
                isColorMode = isColorMode,
                onColorModeChange = { isColorMode ->
                    mainViewModel.updateColorMode(isColorMode)
                }
            )
            AcceptButton(
                modifier = Modifier
                    .padding(start = 14.dp),
                title = "Change color",
                onClick = {
                    showColorPickerBottomSheet = true
                }
            )
        }

        AcceptButton(
            modifier = Modifier
                .padding(vertical = 16.dp),
            title = "See More Shapes",
            onClick = {
                showShapeBottomSheet = true
            }
        )

        if (showShapeBottomSheet) {
            ShapeBottomSheet(
                listShapes = listShapes,
                onShapesSelected = { firstShape, secondShape ->
                    mainViewModel.updateMorphTitle(firstShape.name, secondShape.name)
                    mainViewModel.updateSelectedShapes(firstShape.id, secondShape.id)
                },
                onDismiss = {
                    showShapeBottomSheet = false
                }
            )
        }

        if (showColorPickerBottomSheet) {
            ColorPickerBottomSheet(
                onDismiss = {
                    showColorPickerBottomSheet = false
                },
                onColorsSelected = { colorsPair ->
                    val internalColors = floatArrayOf(
                        colorsPair.first.red,
                        colorsPair.first.green,
                        colorsPair.first.blue,
                    )
                    val externalColors = floatArrayOf(
                        colorsPair.second.red,
                        colorsPair.second.green,
                        colorsPair.second.blue
                    )
                    mainViewModel.updateColorsSelected(internalColors to externalColors)
                }
            )
        }
    }
}