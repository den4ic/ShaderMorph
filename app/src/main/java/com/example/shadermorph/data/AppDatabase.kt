package com.example.shadermorph.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shadermorph.data.AppDatabase.Companion.DB_NAME
import com.example.shadermorph.data.dao.ShapeDao
import com.example.shadermorph.data.model.ShapeDBO

class MorphDatabase internal constructor(
    private val database: AppDatabase
) {
    val shapeDao: ShapeDao
        get() = database.shapeDao()
}

@Database(
    version = AppDatabase.VERSION,
    entities = [
        ShapeDBO::class
    ],
    exportSchema = false
)
internal abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION: Int = 1
        const val DB_NAME: String = "morph_database"
    }

    abstract fun shapeDao(): ShapeDao
}

fun MorphDatabase(applicationContext: Context) : MorphDatabase {
    val incomeRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        AppDatabase::class.java,
        DB_NAME)
        .createFromAsset("database/$DB_NAME.db")
        .fallbackToDestructiveMigration()
        .build()
    return MorphDatabase(incomeRoomDatabase)
}