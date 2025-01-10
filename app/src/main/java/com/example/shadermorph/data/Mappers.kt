package com.example.shadermorph.data

import com.example.shadermorph.data.model.ShapeDBO
import com.example.shadermorph.domain.model.Shape

internal fun Shape.mapToShapeDBO(): ShapeDBO {
    return ShapeDBO(
        id = id,
        name = name
    )
}

internal fun ShapeDBO.toShape(): Shape {
    return Shape(
        id = id,
        name = name
    )
}
