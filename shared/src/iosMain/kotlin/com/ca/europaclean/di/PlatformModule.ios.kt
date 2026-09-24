package com.ca.europaclean.di

import com.ca.europaclean.Platform
import com.ca.europaclean.getPlatform
import org.koin.core.module.Module
import org.koin.dsl.module

private val iosCorePlatformModule = module {
    single<Platform> { getPlatform() }
}

internal fun iosPlatformModule(): Module = module {
    includes(iosCorePlatformModule)
}
