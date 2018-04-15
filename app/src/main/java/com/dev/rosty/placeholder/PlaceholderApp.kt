package com.dev.rosty.placeholder

import android.app.Application
import com.dev.rosty.placeholder.injection.AppComponent
import com.dev.rosty.placeholder.injection.AppModule
import com.dev.rosty.placeholder.injection.DaggerAppComponent

class PlaceholderApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}