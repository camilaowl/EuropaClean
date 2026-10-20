package com.ca.europaclean.di

import com.ca.auth.di.authModule
import com.ca.authentication.di.authenticationModule
import com.ca.datastore.di.dataStoreModule
import com.ca.europaclean.presentation.MainViewModel
import com.ca.network.di.networkModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal fun sharedModule() = module {
    includes(
        dataStoreModule,
        authenticationModule(),
        networkModule(),
        authModule,
        appModule,
    )
}

internal val appModule = module {
    viewModel { MainViewModel(get()) }
}
