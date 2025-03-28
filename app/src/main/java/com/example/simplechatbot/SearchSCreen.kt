package com.example.simplechatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.simplechatbot.ui.theme.ChatBubble
import kotlinx.coroutines.launch

@Composable
fun SearchScreen( navController: NavController,viewModel: ChatbotViewModel = viewModel()) {
    var userInput by remember { mutableStateOf("") }
    val chatHistory by viewModel.chatHistory.collectAsState()
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }



    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).windowInsetsPadding(WindowInsets.ime)) {

        // 1st row
        Row(Modifier.fillMaxWidth().padding(start = 2.dp, top = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            IconButton(
                onClick = { navController.navigate(Screen.HomeScreen.route) },
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.DarkGray, shape = CircleShape)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    tint = Color.White,
                    contentDescription = "Go back",
                    modifier = Modifier.size(20.dp)
                )
            }

            Text("Chat", fontSize = 32.sp, color = Color.White)

            Row {
                IconButton(
                    onClick =  { viewModel.saveCurrentSession()
                        viewModel.startNewChat()},
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.DarkGray, shape = CircleShape)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = "Clear chat",
                        modifier = Modifier.size(24.dp)
                    )
                }

//                IconButton(onClick = { }) {
//                    Icon(
//                        imageVector = Icons.Default.Share,
//                        tint = Color.White,
//                        contentDescription = "Share chat",
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Divider()

        // Lazy column for messages
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            reverseLayout = true    // latest message appear at bottom
        ) {
            items(chatHistory.reversed()){
                message ->
                ChatBubble(message)
            }
        }
        Spacer(Modifier.height(16.dp))


        // 3rd row - TextField
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                TextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    placeholder = { Text("Ask anything...", fontSize = 18.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester).imePadding(),
                    textStyle = TextStyle(fontSize = 18.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0XFF2A292B),
                        unfocusedContainerColor = Color(0XFF2A292B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White
                    )
                )


                if (userInput.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            viewModel.sendMessageToChatbot(userInput)
                            userInput = ""
                            coroutineScope.launch {
                                scrollState.animateScrollToItem(0)
                            }
                        },
                        modifier = Modifier
                            .background(Color.DarkGray, shape = CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            tint = Color.White,
                            contentDescription = "Send message",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        Row(
            Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End) {
            Text("gemini-2.0-flash model", fontSize = 15.sp,color = Color.Gray)
        }


    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}