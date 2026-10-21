package com.ca.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val dataStoreModule = module {
    single<DataStore<Preferences>> {
        val context = androidContext()

        createDataStore(
            storage = FileStorage(
                serializer = PreferencesFileSerializer,
                produceFile = {
                    context.filesDir.resolve(DATA_STORE_FILE_NAME)
                },
            ),
        )
    }
}
