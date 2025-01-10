package com.example.shadermorph.data

import com.example.shadermorph.data.model.ShapeDBO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShapeRepository @Inject constructor(
    private val database: MorphDatabase
) {
    fun getAll(): Flow<List<ShapeDBO>> {
        return database.shapeDao.getAll()
    }
}