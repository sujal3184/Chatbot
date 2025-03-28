package com.example.simplechatbot.ui.theme

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplechatbot.ChatMessage

@Composable
fun ChatBubble(message: ChatMessage){

    var showDropdown by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(message.isUser) Arrangement.End else Arrangement.Start,
    ) {
        if (message.isLoading) {
            // Loading indicator for Gemini responses
            Box(
                modifier = Modifier
                    .background(
                        Color.DarkGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
                    .widthIn(max = 250.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingDots()
            }
        } else {
            // Regular message bubble
            Box(
                modifier = Modifier
                    .background(
                        if (message.isUser) Color(0XFF2A292B) else Color.DarkGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
                    .widthIn(max = 250.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                showDropdown = true
                            }
                        )
                    }
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 16.sp
                )

                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    // Position the dropdown based on whether it's a user or AI message
                    modifier = Modifier.align(
                        if(message.isUser) Alignment.TopEnd else Alignment.TopStart
                    )
                ) {
                    DropdownMenuItem(
                        text = { Text("Copy", color = Color.White)  },
                        onClick = {
                            clipboardManager.setText(AnnotatedString(message.text))
                            showDropdown = false
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy"
                            )
                        }
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(24.dp))
}

@Composable
fun LoadingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinite dots transition")

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { index ->
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 800,
                        easing = FastOutLinearInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "Dot ${index + 1} alpha"
            )

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(alpha)
                    .background(Color.White, shape = CircleShape)
            )
        }
    }
}