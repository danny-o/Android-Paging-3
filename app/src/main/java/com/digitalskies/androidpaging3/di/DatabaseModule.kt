package com.digitalskies.androidpaging3.di

import android.content.Context
import androidx.room.Room
import com.digitalskies.androidpaging3.data.ArticlesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideRoomDatabase(@ApplicationContext application: Context):ArticlesDatabase{
        return Room.databaseBuilder(application,ArticlesDatabase::class.java,
            ArticlesDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }



}