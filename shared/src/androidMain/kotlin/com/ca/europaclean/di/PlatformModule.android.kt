package com.ca.europaclean.di

import com.ca.authentication.di.googleAuthModule
import com.ca.europaclean.Platform
import com.ca.europaclean.getPlatform
import org.koin.core.module.Module
import org.koin.dsl.module

private val androidCorePlatformModule = module {
    single<Platform> { getPlatform() }
}

fun androidPlatformModule(googleWebClientId: String): Module = module {
    includes(
        androidCorePlatformModule,
        googleAuthModule(googleWebClientId),
    )
}
