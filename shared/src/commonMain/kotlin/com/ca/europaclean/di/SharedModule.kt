package com.ca.europaclean.di

import com.ca.auth.di.authModule
import com.ca.datastore.di.dataStoreModule
import com.ca.network.di.networkModule
import org.koin.dsl.module

internal fun sharedModule() = module {
    includes(
        dataStoreModule,
        networkModule(),
        authModule,
    )
}
