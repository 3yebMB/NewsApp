package dev.m13d.newsapp.di.app

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    companion object{
        lateinit var appComponent: AppComponent
    }
}
