package com.example.dogapp.di

import android.app.Application
import androidx.room.Room
import com.example.dogapp.room.DogAppDatabase
import com.example.dogapp.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Proporciono la instancia única de la base de datos dogapp-database
    @Provides
    @Singleton
    fun provideDogAppDatabase(application: Application): DogAppDatabase {
        return Room.databaseBuilder(
            application,
            DogAppDatabase::class.java,
            "dogapp-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: DogAppDatabase): UserDao {
        return appDatabase.userDao()
    }

}