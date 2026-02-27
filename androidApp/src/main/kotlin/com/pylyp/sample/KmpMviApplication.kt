package com.pylyp.sample

import android.app.Application
import io.pylyp.sample.composeapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class KmpMviApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@KmpMviApplication)
        }
    }
}

