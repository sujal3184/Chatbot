package com.example.simplechatbot

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.simplechatbot.AuthPages.ProfileScreen
import com.example.simplechatbot.AuthPages.SignupPage
import com.example.simplechatbot.auth.AuthViewModel
import com.example.simplechatbot.auth.LoginPage
import com.example.simplechatbot.googleSignIn.AuthState
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: ChatbotViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    // Observe authentication state
    val authState = authViewModel.authState.observeAsState()
    val googleSignInState by authViewModel.googleSignInState.collectAsState()

    // Handle back press behavior for HomeScreen and LoginScreen
    val activity = LocalActivity.current as? Activity

    // Check and handle authentication state changes
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                // Navigate to home screen when user is authenticated
                if (navController.currentDestination?.route == Screen.LoginScreen.route ||
                    navController.currentDestination?.route == Screen.SignupScreen.route) {
                    navController.navigate(Screen.HomeScreen.route) {
                        // Clear back stack so user can't go back to login
                        popUpTo(0)
                    }
                }
            }
            is AuthState.Unauthenticated -> {
                // Do nothing, start destination will handle this
            }
            else -> {}
        }
    }

    // Listen for back press events
    BackHandler(enabled = navController.currentBackStackEntry?.destination?.route == Screen.HomeScreen.route) {
        // Exit app when back is pressed from HomeScreen
        activity?.finish()
    }

    BackHandler(enabled = navController.currentBackStackEntry?.destination?.route == Screen.LoginScreen.route) {
        // Exit app when back is pressed from Login
        activity?.finish()
    }

    // Determine start destination based on authentication status
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        Screen.HomeScreen.route
    } else {
        Screen.LoginScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication routes
        composable(Screen.LoginScreen.route) {
            LoginPage(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Screen.SignupScreen.route) {
            SignupPage(navController, authViewModel)
        }

        composable(Screen.ProfieScreen.route) {
            ProfileScreen(navController, authViewModel)
        }

        // App routes
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