package com.example.shadermorph.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.example.shadermorph.domain.model.Shape
import com.example.shadermorph.ui.theme.Green
import com.example.shadermorph.ui.theme.LightBlue

@Composable
fun ShapeCard(
    shapeItem: Shape,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isSecondSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> LightBlue
        isSecondSelected -> Green
        else -> White
    }

    Box(
        modifier = modifier
            .padding(6.dp)
            .border(
                width = 1.dp,
                color = Black,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(
            text = shapeItem.name ?: "",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Center),
            color = Black
        )
    }
}