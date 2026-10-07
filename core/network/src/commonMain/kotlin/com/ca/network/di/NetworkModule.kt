package com.ca.network.di

import com.ca.network.createHttpClient
import org.koin.dsl.module
import org.koin.dsl.onClose

fun networkModule() = module {
    single {
        createHttpClient(get())
    } onClose { client ->
        client?.close()
    }
}
