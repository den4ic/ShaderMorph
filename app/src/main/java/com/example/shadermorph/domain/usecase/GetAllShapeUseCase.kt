package com.example.shadermorph.domain.usecase

import com.example.shadermorph.data.ShapeRepository
import com.example.shadermorph.data.toShape
import com.example.shadermorph.domain.model.Shape
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.example.shadermorph.utils.Result
import javax.inject.Inject

class GetAllShapeUseCase @Inject constructor(
    private val shapeRepository: ShapeRepository
) {
    operator fun invoke(): Flow<Result<List<Shape>>> = shapeRepository.getAll()
        .map { shapeDBOs ->
            val shape = shapeDBOs.map { it.toShape () }
            Result.Success(shape) as Result<List<Shape>>
        }
        .catch { e ->
            emit(Result.Failure(e))
        }
}