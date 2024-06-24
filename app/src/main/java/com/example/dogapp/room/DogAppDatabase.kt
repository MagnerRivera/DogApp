package com.example.dogapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class DogAppDatabase : RoomDatabase()  {

    abstract fun userDao(): UserDao

}