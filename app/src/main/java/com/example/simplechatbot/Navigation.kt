package com.example.simplechatbot

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.simplechatbot.AuthPages.AuthViewModel
import com.example.simplechatbot.AuthPages.LoginPage
import com.example.simplechatbot.AuthPages.ProfileScreen
import com.example.simplechatbot.AuthPages.SignupPage


@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: ChatbotViewModel = viewModel(),
    authViewModel: AuthViewModel = AuthViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        // authentication

        composable(Screen.LoginScreen.route) {
            LoginPage(navController,authViewModel)
        }

        composable(Screen.SignupScreen.route) {
            SignupPage(navController,authViewModel)
        }

        composable(Screen.ProfieScreen.route) {
            ProfileScreen(navController,authViewModel)
        }



        // orig
        composable(Screen.HomeScreen.route) {
            MainScreen(navController, authViewModel)
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(navController, sharedViewModel)
        }
        composable(Screen.ChatHistoryScreen.route) {
            HistoryScreen(navController, sharedViewModel)
        }
        composable(
            route = Screen.ChatDetailScreen.route + "/{sessionId}",
            arguments = listOf(
                navArgument("sessionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId")
            val session = sharedViewModel.savedSessions.value.find { it.chatID.toString() == sessionId.toString() }
            session?.let {
                Historychats(navController, it.chatMessage)
            }
        }
    }
}