package com.ca.authentication.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.FileStorage
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import androidx.datastore.preferences.core.emptyPreferences
import com.ca.authentication.AndroidTokenStorage
import com.ca.authentication.TokenStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val authTokens = named("authTokens")

fun androidTokenStorageModule() = module {
    single<DataStore<Preferences>>(authTokens) {
        DataStoreFactory.create(
            storage = FileStorage(
                serializer = PreferencesFileSerializer,
                produceFile = {
                    androidApplication()
                        .noBackupFilesDir
                        .resolve("auth_tokens.preferences_pb")
                },
            ),
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        )
    }

    single<TokenStorage> {
        AndroidTokenStorage(get(authTokens))
    }
}
