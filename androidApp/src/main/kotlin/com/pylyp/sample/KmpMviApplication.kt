package com.pylyp.sample

import android.app.Application
import com.pylyp.sample.di.androidAppModule
import io.pylyp.sample.composeapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class KmpMviApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(platformAppModules = listOf(androidAppModule)) {
            androidLogger()
            androidContext(this@KmpMviApplication)
        }
    }
}

