package com.example.simplechatbot

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainScreen(
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 24.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Row(modifier= Modifier.fillMaxWidth().padding(top = 16.dp, start = 4.dp, end = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
            }

            Text("Chatbot",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
           Text("       ")


        }

        Spacer(modifier = Modifier.weight(1f))
        Row {
            Text("HOW CAN I",color = Color.DarkGray, textAlign = TextAlign.Center, fontSize = 60.sp)
        }
        Row {
            Text("HELP YOU",color = Color.DarkGray, textAlign = TextAlign.Center, fontSize = 60.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row (modifier= Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Button(
                onClick = {navController.navigate(Screen.SearchScreen.route)},
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.DarkGray
                )
            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Message Chatbot ...", color = Color.White, fontSize = 24.sp)
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (modifier= Modifier.fillMaxWidth().padding( bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            IconButton(onClick = {navController.navigate(Screen.HomeScreen.route)}) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text("|", fontSize = 42.sp, color = Color.White)

            IconButton(onClick = {navController.navigate(Screen.ChatHistoryScreen.route)}) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }

        }



    }

}
