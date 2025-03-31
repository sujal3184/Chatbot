package com.example.simplechatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechatbot.Data.Session
import com.example.simplechatbot.Data.SessionRepository
import com.example.simplechatbot.ui.theme.ApiIntegrator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ChatbotViewModel(
    private val sessionRepository: SessionRepository = Graph.sessionRepository,
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory

    // Persistent context to maintain conversation state
    private var conversationContext = mutableListOf<String>()

    // Current active session
    private var currentSession: Session? = null

    // Available sessions
    private val _savedSessions = MutableStateFlow<List<Session>>(emptyList())
    val savedSessions: StateFlow<List<Session>> = _savedSessions

    init {
        // Load existing sessions when ViewModel is created
        loadSavedSessions()
    }

    fun sendMessageToChatbot(message: String) {
        viewModelScope.launch {
            // Add user message
            _chatHistory.value = _chatHistory.value + ChatMessage(message, isUser = true)

            // Add loading indicator for chatbot response
            _chatHistory.value = _chatHistory.value + ChatMessage("", isUser = false, isLoading = true)

            try {
                // Get response from API with accumulated context
                val response = ApiIntegrator.getChatbotResponse(message, conversationContext)

                // Remove loading indicator and add actual response
                _chatHistory.value = _chatHistory.value
                    .filterNot { it.isLoading && !it.isUser }
                    .plus(ChatMessage(response, isUser = false))

                // Update conversation context
                conversationContext.add(response)
            } catch (e: Exception) {
                // Handle error - replace loading indicator with error message
                _chatHistory.value = _chatHistory.value
                    .filterNot { it.isLoading && !it.isUser }
                    .plus(ChatMessage("Error: ${e.localizedMessage}", isUser = false))
            }
        }
    }

    // Method to start a new chat
    fun startNewChat() {
        // Clear the chat history
        _chatHistory.value = emptyList()

        // Clear the conversation context
        conversationContext.clear()

        // Create a new session
        currentSession = null
    }

    // Update this function to save with the current user's ID
    fun saveCurrentSession(title: String? = null) {
        viewModelScope.launch {
            val chatMessages = _chatHistory.value

            // Get current user ID
            val currentUserID = auth.currentUser?.uid ?: return@launch

            // Generate a title if not provided
            val sessionTitle = title ?: generateSessionTitle(chatMessages)

            // Create a new session with user ID
            val session = Session(
                userID = currentUserID, // Add user ID
                chatTitle = sessionTitle,
                chatMessage = chatMessages
            )

            // Save the session
            if(chatMessages.isNotEmpty()) sessionRepository.insertSession(session)

            // Reload saved sessions
            loadSavedSessions()
        }
    }
    // Load saved sessions
    fun loadSavedSessions() {
        viewModelScope.launch {
            val currentUserID = auth.currentUser?.uid

            if (currentUserID != null) {
                // Get only the current user's sessions
                val sessions = sessionRepository.getSessionsByUserID(currentUserID)
                _savedSessions.value = sessions
            } else {
                // No user is signed in
                _savedSessions.value = emptyList()
            }
        }
    }

    // Delete a session
    fun deleteSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.deleteSession(session)
            loadSavedSessions()
        }
    }

    // Add this method to your ViewModel to load a selected session
    fun loadSelectedSession(session: Session) {
        // Clear current chat history
        _chatHistory.value = emptyList()

        // Load messages from the selected session
        _chatHistory.value = session.chatMessage

        // Optionally, restore conversation context
        conversationContext.clear()
        conversationContext.addAll(
            session.chatMessage
                .filter { !it.isUser }
                .map { it.text }
        )
    }

    // Generate a default title for the session
    private fun generateSessionTitle(messages: List<ChatMessage>): String {
        // Generate a title based on the first user message or a default
        return messages.firstOrNull { it.isUser }?.text?.take(30) ?: "New Chat Session"
    }

}