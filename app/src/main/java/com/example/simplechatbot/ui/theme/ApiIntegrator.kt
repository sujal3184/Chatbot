package com.example.simplechatbot.ui.theme

import com.example.simplechatbot.BuildConfig.API_KEY
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiIntegrator {

    private val generativeModel = GenerativeModel(modelName = "gemini-2.0-flash", apiKey = API_KEY)

    suspend fun getChatbotResponse(
        prompt: String,
        previousContext: List<String> = emptyList()
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                // Construct an enhanced prompt with previous context
                val enhancedPrompt = buildPromptWithContext(prompt, previousContext)

                val response = generativeModel.generateContent(enhancedPrompt)
                response.text ?: "No Response Generated"
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }

    private fun buildPromptWithContext(
        currentPrompt: String,
        previousContext: List<String>
    ): String {
        // If there's previous context, incorporate it into the prompt
        return if (previousContext.isNotEmpty()) {
            val contextString = previousContext.joinToString(", ")
            "Previous responses: $contextString. New request: $currentPrompt. " +
                    "Ensure the new response builds upon or relates to the previous context."
        } else {
            currentPrompt
        }
    }
}