package com.example.simplechatbot.AuthPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.simplechatbot.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController : NavController, authViewModel: AuthViewModel) {
    val currentUser = FirebaseAuth.getInstance().currentUser ?: null
    val showLogoutDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),

        ) {
        Row(
            Modifier.padding(top = 32.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.navigate(Screen.HomeScreen.route) },
                modifier = Modifier
                    .padding(2.dp)
                    .background(Color.White, shape = CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.DarkGray,
                    contentDescription = "Go back",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.widthIn(min = 80.dp, max = 100.dp))
            Text(
                text = "Profile",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }


        Spacer(modifier = Modifier.height(100.dp))

        Row(Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Email",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Row(Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.Center) {
            Text(
                text = currentUser?.email ?: "Not signed in",
                fontSize = 18.sp,
                color = Color.White
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center) {
            TextButton(
                onClick = {
                    // Show confirmation dialog instead of immediately logging out
                    showLogoutDialog.value = true
                },
                modifier = Modifier.size(100.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Log Out ", fontWeight = FontWeight.ExtraBold)
            }
        }
    }

    // Custom styled logout confirmation dialog
    if (showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog.value = false
            },
            title = {
                Text(
                    "Logout",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "Are you really want to log out?",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            containerColor = Color(0XFF2A292B),
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            confirmButton = {
            },
            dismissButton = {
                Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(
                        onClick = {
                            showLogoutDialog.value = false
                        },
                        modifier = Modifier.size(75.dp,50.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White,
                            containerColor = Color.DarkGray
                        )
                    ) {
                        Text("No", fontSize = 20.sp)
                    }
                    TextButton(
                        onClick = {
                            authViewModel.signout()
                            showLogoutDialog.value = false
                            navController.navigate(Screen.LoginScreen.route){
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.size(75.dp,50.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White,
                            containerColor = Color.DarkGray
                        )
                    ) {
                        Text("Yes", fontSize = 20.sp)
                    }
                }

            }
        )
    }
}