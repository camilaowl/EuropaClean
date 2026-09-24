package com.ca.authentication.di

import androidx.credentials.CredentialManager
import com.ca.authentication.GoogleAuth
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

fun googleAuthModule(webClientId: String) = module {
    single<CredentialManager> { CredentialManager.create(androidApplication()) }
    single<GoogleAuth> { GoogleAuth(webClientId, get()) }
}
