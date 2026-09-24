package com.ca.europaclean.di

import com.ca.auth.di.authModule
import org.koin.dsl.module

internal fun sharedModule() = module {
    includes(authModule)
}
