package com.example.simplechatbot

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.simplechatbot.Data.SessionDataBase
import com.example.simplechatbot.Data.SessionRepository

object Graph {

    lateinit var database: SessionDataBase

    val sessionRepository by lazy {
        SessionRepository(sessionDao = database.sessionDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context, SessionDataBase::class.java, "chatbot.db")
            .build()
    }
}
class ChatbotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(applicationContext)
    }
}