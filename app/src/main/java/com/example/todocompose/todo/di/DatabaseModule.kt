package com.example.todocompose.todo.di

import android.app.Application
import androidx.room.Room
import com.example.todocompose.todo.data.TodoDao
import com.example.todocompose.todo.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): TodoDatabase =
        Room.databaseBuilder(application,TodoDatabase::class.java,"TodoDatabase")
            .fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun providesDao(db:TodoDatabase): TodoDao = db.getDao()

}