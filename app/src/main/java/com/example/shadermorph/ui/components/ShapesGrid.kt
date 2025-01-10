package com.example.shadermorph.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shadermorph.domain.model.Shape

@Composable
fun ShapesGrid(
    listShapes: List<Shape>,
    selectedShapes: Pair<Shape?, Shape?>,
    onShapeSelected: (Shape) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        items(listShapes, key = { item -> item.id }) { item ->
            val isFirstSelected = selectedShapes.first == item
            val isSecondSelected = selectedShapes.second == item

            ShapeCard(
                shapeItem = item,
                isSelected = isFirstSelected,
                isSecondSelected = isSecondSelected,
                onClick = {
                    onShapeSelected(item)
                }
            )
        }
    }
}