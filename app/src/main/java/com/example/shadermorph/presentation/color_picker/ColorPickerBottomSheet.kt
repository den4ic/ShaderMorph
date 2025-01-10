package com.example.shadermorph.presentation.color_picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shadermorph.domain.model.TileState
import com.example.shadermorph.ui.components.AcceptButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ColorPickerBottomSheet(
    onDismiss: () -> Unit,
    onColorsSelected: (Pair<Color, Color>) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            newState != SheetValue.Expanded
        }
    )

    var selectedColor by remember { mutableStateOf(Color.White) }

    val tiles = remember { mutableStateListOf(
        TileState("Fill shape color", Color.Black, true),
        TileState("Inner shape color", Color.Black, false)
    ) }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = {  },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                text = "Select color",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "First, select the inner color, then the outer color for filling.",
                fontSize = 14.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                ColorPickerView(
                    onColorPicked = {
                        selectedColor = it
                    }
                )
            }

            tiles.forEachIndexed { index, tile ->
                tiles[index] = tile.copy(
                    color = if (tile.isSelected) selectedColor else tile.color,
                    isSelected = tile.isSelected || index == tiles.indexOfFirst { it.isSelected }
                )

                ColorPickerCard(
                    text = tile.text,
                    boxColor = tiles[index].color,
                    isSelected = tiles[index].isSelected,
                    onClick = {
                        tiles.forEachIndexed { i, _ ->
                            tiles[i] = tiles[i].copy(isSelected = i == index)
                        }
                    }
                )
            }

            AcceptButton(
                modifier = Modifier.padding(top = 12.dp),
                title = "Accept change",
                onClick = {
                    val selectedColors = Pair(tiles[1].color, tiles[0].color)
                    onColorsSelected(selectedColors)
                    onDismiss()
                },
                isVisibilityIcon = false
            )
        }
    }
}