package com.ca.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module

internal const val DATA_STORE_FILE_NAME = "europa_clean.preferences_pb"

fun createDataStore(storage: Storage<Preferences>): DataStore<Preferences> =
    DataStoreFactory.create(storage = storage)

expect val dataStoreModule: Module
