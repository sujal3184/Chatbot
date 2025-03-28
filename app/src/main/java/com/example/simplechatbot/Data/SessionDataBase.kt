package com.example.simplechatbot.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Session::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SessionDataBase : RoomDatabase() {
    abstract fun sessionDao(): SessionDAO
}