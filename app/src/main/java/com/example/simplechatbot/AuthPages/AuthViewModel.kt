package com.example.simplechatbot.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechatbot.googleSignIn.AuthState
import com.example.simplechatbot.googleSignIn.SignInResult
import com.example.simplechatbot.googleSignIn.SignInState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // Firebase Auth instance
    private val _auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Regular email/password auth state
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // Google Sign-In state
    private val _googleSignInState = MutableStateFlow(SignInState())
    val googleSignInState = _googleSignInState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (_auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    fun signOut() {
        viewModelScope.launch {
            // Sign out from both regular auth and Google
            _auth.signOut()
            _authState.value = AuthState.Unauthenticated
            resetGoogleSignInState()
        }
    }

    // Google Sign-In related functions
    fun onGoogleSignInResult(result: SignInResult) {
        _googleSignInState.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }

        // Update the main auth state as well if successful
        if (result.data != null) {
            _authState.value = AuthState.Authenticated
        }
    }

    fun resetGoogleSignInState() {
        _googleSignInState.update { SignInState() }
    }
}