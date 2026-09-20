package com.ca.europaclean.di

import com.ca.europaclean.Greeting
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val applicationModule = module {
    singleOf(::Greeting)
}

internal expect val platformModule: Module

internal fun appModules(): List<Module> = listOf(
    applicationModule,
    platformModule,
)
