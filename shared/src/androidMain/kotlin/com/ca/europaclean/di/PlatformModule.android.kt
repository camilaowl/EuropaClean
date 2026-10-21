package com.ca.europaclean.di

import com.ca.authentication.di.googleAuthModule
import com.ca.authentication.di.androidTokenStorageModule
import com.ca.europaclean.Platform
import com.ca.europaclean.getPlatform
import com.ca.network.NetworkConfig
import org.koin.core.module.Module
import org.koin.dsl.module

private val androidCorePlatformModule = module {
    single<Platform> { getPlatform() }
    single {
        NetworkConfig(baseUrl = "http://127.0.0.1:3000")
    }
}

fun androidPlatformModule(googleWebClientId: String): Module = module {
    includes(
        androidCorePlatformModule,
        androidTokenStorageModule(),
        googleAuthModule(googleWebClientId),
    )
}
