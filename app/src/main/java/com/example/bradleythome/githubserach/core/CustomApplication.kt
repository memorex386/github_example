package com.example.bradleythome.githubserach.core

import com.example.bradleythome.githubserach.BuildConfig
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

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        applyAutoInjector()
    }
}
