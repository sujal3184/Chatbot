package com.example.simplechatbot.AuthPages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simplechatbot.Screen

@Composable
fun LoginPage( navController: NavController, authViewModel: AuthViewModel){

    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }

    val authState = authViewModel.authstate.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(0)
                }
            }
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login Page", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(value = email,
            onValueChange = {email = it},
            label = { Text("Email")})

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = password,
            onValueChange = {password = it},
            label = { Text("Password")})

        Spacer(Modifier.height(32.dp))

        Button(onClick = {
            authViewModel.login(email,password)
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            enabled = authState.value != AuthState.Loading
            ){
            Text("Login", fontWeight = FontWeight.ExtraBold)
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate(Screen.SignupScreen.route)
        }) {
            Text("Don't have an account? Sign up", fontSize = 20.sp)
        }

    }
}