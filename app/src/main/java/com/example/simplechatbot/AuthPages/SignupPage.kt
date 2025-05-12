package com.example.simplechatbot.AuthPages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simplechatbot.Screen
import com.example.simplechatbot.auth.AuthViewModel
import com.example.simplechatbot.googleSignIn.AuthState

@Composable
fun SignupPage(navController: NavController, authViewModel: AuthViewModel){
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(Screen.HomeScreen.route){
                    popUpTo(0)
                }
            }
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message,Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Signup Page", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") })

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            })

        Spacer(Modifier.height(32.dp))

        Button(onClick = {
            authViewModel.signup(email,password)
        },
            colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
            enabled = authState.value != AuthState.Loading
        ) {
            Text("Create Account", fontWeight = FontWeight.ExtraBold)
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate(Screen.LoginScreen.route)
        }) {
            Text("Already have an account? Login", fontSize = 20.sp)
        }
    }
}