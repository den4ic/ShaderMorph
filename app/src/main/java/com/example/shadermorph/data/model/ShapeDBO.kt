package com.example.shadermorph.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ShapeDBO.TABLE_NAME)
data class ShapeDBO(
    @PrimaryKey var id: Int = 0,
    @ColumnInfo(name = "name") var name: String?,
) {
    companion object {
        const val TABLE_NAME: String = "shape"
    }
}