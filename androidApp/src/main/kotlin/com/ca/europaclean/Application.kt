package com.ca.europaclean

import android.app.Application
import com.ca.europaclean.di.androidPlatformModule
import com.ca.europaclean.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class EuropaCleanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin(
            platformModule = androidPlatformModule(
                googleWebClientId = getString(R.string.google_web_client_id),
            ),
            config = {
                androidContext(this@EuropaCleanApplication)
                androidLogger()
            },
        )
    }
}
