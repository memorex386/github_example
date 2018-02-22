package com.example.bradleythome.githubserach.core

import com.example.bradleythome.githubserach.BuildConfig
import com.squareup.moshi.Moshi
import timber.log.Timber

/**
 * Created by bradley.thome on 10/16/17.
 */
class CustomApplication : DaggerApplication() {

    override fun applicationInjector() = DaggerAppComponent.builder()
            .application(this)
            .set(AppModule(this))
            .build()

    override fun onCreate() {
        super.onCreate()

        app = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        applyAutoInjector()
    }
}

lateinit var app: CustomApplication
    private set

lateinit var moshi: Moshi
