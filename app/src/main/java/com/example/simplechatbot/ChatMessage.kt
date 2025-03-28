package com.example.simplechatbot


data class ChatMessage(val text: String, val isUser: Boolean, val isLoading: Boolean = false)
