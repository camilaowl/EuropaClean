package com.ca.europaclean.di

import com.ca.europaclean.Platform
import com.ca.europaclean.getPlatform
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single<Platform> { getPlatform() }
}
