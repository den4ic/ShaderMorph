package com.example.shadermorph.di

import android.content.Context
import com.example.shadermorph.data.MorphDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideMorphDatabase(@ApplicationContext context: Context): MorphDatabase {
        return MorphDatabase(context)
    }
}