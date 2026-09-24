package com.ca.europaclean.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(
    platformModule: Module,
    config: KoinAppDeclaration? = null,
): KoinApplication {
    return startKoin {
        includes(config)
        modules(
            platformModule,
            sharedModule()
        )
    }
}
