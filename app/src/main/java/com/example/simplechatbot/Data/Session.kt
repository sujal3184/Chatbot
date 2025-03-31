package com.example.simplechatbot.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplechatbot.ChatMessage


@Entity(tableName = "chatSessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val chatID: Int = 0,

    @ColumnInfo(name = "userID")
    val userID: String = "",  // Add this field to store Firebase user ID

    @ColumnInfo(name = "Chat Title")
    val chatTitle: String = "",

    @ColumnInfo(name = "ChatMessages")
    val chatMessage: List<ChatMessage> = emptyList()
)