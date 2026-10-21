package com.ca.europaclean.di

import com.ca.authentication.di.iosTokenStorageModule
import com.ca.europaclean.Platform
import com.ca.europaclean.getPlatform
import com.ca.network.NetworkConfig
import org.koin.core.module.Module
import org.koin.dsl.module

private val iosCorePlatformModule = module {
    single<Platform> { getPlatform() }
    single { NetworkConfig() }
}

internal fun iosPlatformModule(): Module = module {
    includes(
        iosCorePlatformModule,
        iosTokenStorageModule(),
    )
}
