package com.example.bradleythome.githubserach.core

import android.app.Application
import android.content.Context
import android.location.LocationManager
import dagger.Module
import dagger.Provides

/**
 * Created by brad.thome.
 *
 * This Module provides core dependencies needed throughout the app.
 */


@Module(includes = arrayOf())
class AppModule(private var app: Application) {

    @ApplicationScope
    @Provides
    fun provideApplication(): Application = app

    //To Inject the ApplicationContext, use the @Field annotation
    //@field:[Inject ForApplication]
    //lateinit var appContext: Context
    @Provides
    @ForApplication
    @ApplicationScope
    fun provideApplicationContext(): Context = app.applicationContext

    @Provides
    @ApplicationScope
    fun provideLocationManager(): LocationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
}
