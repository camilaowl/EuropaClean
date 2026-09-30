package com.ca.authentication

sealed interface GoogleSignInResult {
    data class Success(val idToken: String) : GoogleSignInResult
    data object NoCredential : GoogleSignInResult
    data object Cancelled : GoogleSignInResult
    data object Failed : GoogleSignInResult
}