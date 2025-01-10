package com.example.shadermorph.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shadermorph.data.model.ShapeDBO
import kotlinx.coroutines.flow.Flow

/** Get all shapes out
listOf(
    Shape(0, "Circle"),
    Shape(1, "Square"),
    Shape(2, "Rectangle"),
    Shape(3, "Oval"),
    Shape(4, "Diamond"),
    Shape(5,"Parallelogram"),
    Shape(6, "Triangle"),
    Shape(7, "Pentagon"),
    Shape(8, "Hexagon"),
    Shape(9, "Heptagon"),
    Shape(10, "Octagon"),
    Shape(11, "Nonagon"),
    Shape(12, "Decagon"),
    Shape(13, "Trapezoid"),
    Shape(14, "Capsule"),
    Shape(15, "Heart"),
    Shape(16, "Star"),
    Shape(17, "Crescent")
)
 */
@Dao
interface ShapeDao {
    @Query("SELECT * FROM ${ShapeDBO.TABLE_NAME}")
    fun getAll(): Flow<List<ShapeDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shapes: List<ShapeDBO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shape: ShapeDBO)

    @Delete
    suspend fun delete(shape: ShapeDBO)

    @Query("DELETE FROM ${ShapeDBO.TABLE_NAME}")
    suspend fun deleteAll()
}