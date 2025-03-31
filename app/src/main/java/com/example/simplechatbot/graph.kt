package com.example.simplechatbot

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.simplechatbot.Data.SessionDataBase
import com.example.simplechatbot.Data.SessionRepository

object Graph {

    lateinit var database: SessionDataBase

    val sessionRepository by lazy {
        SessionRepository(sessionDao = database.sessionDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context, SessionDataBase::class.java, "chatbot.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Add the new userID column to the chatSessions table
            db.execSQL("ALTER TABLE chatSessions ADD COLUMN userID TEXT NOT NULL DEFAULT ''")
        }
    }
}
class ChatbotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(applicationContext)
    }
}