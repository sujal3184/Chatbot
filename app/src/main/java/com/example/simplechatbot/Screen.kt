package com.example.simplechatbot

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object SearchScreen : Screen("search_screen")
    object ChatHistoryScreen : Screen("chat_history")
    object ChatDetailScreen : Screen("history_chats")
}