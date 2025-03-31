package com.example.simplechatbot

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object ProfieScreen : Screen("profile_screen")
    object HomeScreen : Screen("home_screen")
    object SearchScreen : Screen("search_screen")
    object ChatHistoryScreen : Screen("chat_history")
    object ChatDetailScreen : Screen("history_chats")
}