package com.example.simplechatbot.Data


import androidx.room.TypeConverter
import com.example.simplechatbot.ChatMessage
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromChatMessageList(value: List<ChatMessage>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toChatMessageList(value: String): List<ChatMessage>? {
        val listType = object : com.google.gson.reflect.TypeToken<List<ChatMessage>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
