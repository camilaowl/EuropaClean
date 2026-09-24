package com.ca.auth.di

import com.ca.auth.data.AuthClient
import com.ca.auth.data.AuthRepositoryImpl
import com.ca.auth.domain.AuthRepository
import com.ca.auth.presentation.signin.SignInViewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel

val authModule = module {
    single<AuthRepositoryImpl>() bind AuthRepository::class
    single<AuthClient>()
    viewModel<SignInViewModel>()
}