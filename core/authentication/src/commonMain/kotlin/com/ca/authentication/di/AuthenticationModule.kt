package com.ca.authentication.di

import com.ca.authentication.AuthSessionRepository
import org.koin.dsl.module

fun authenticationModule() = module {
    single<AuthSessionRepository> { AuthSessionRepository(get()) }
}
