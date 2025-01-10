package com.example.shadermorph.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shadermorph.domain.model.Shape
import com.example.shadermorph.ui.components.AcceptButton
import com.example.shadermorph.ui.components.ShapesGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeBottomSheet(
    listShapes: List<Shape>,
    onShapesSelected: (Shape, Shape) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedShapes by remember { mutableStateOf<Pair<Shape?, Shape?>>(null to null) }

    ModalBottomSheet(
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
                text = "Select shape",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "First, select the first shape, then the second one to which the morph will be applied.",
                fontSize = 14.sp
            )

            ShapesGrid(
                listShapes = listShapes,
                selectedShapes = selectedShapes,
                onShapeSelected = { selectedShape ->
                    selectedShapes = when {
                        selectedShapes.first == null -> selectedShape to selectedShapes.second
                        selectedShapes.second == null -> selectedShapes.first to selectedShape
                        else -> selectedShape to null
                    }
                }
            )

            AcceptButton(
                title = "Accept change",
                onClick = {
                    selectedShapes.first?.let { firstShape ->
                        selectedShapes.second?.let { secondShape ->
                            onShapesSelected(firstShape, secondShape)
                        }
                    }
                    onDismiss()
                },
                isVisibilityIcon = false
            )
        }
    }
}