package org.akshar.camera

import android.app.Application
import org.akshar.camera.di.initKoin

class MuseumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}