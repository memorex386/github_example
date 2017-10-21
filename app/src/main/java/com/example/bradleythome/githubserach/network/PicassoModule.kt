package com.example.bradleythome.githubserach.network

import android.content.Context
import com.example.bradleythome.githubserach.core.AppModule
import com.example.bradleythome.githubserach.core.ApplicationScope
import com.example.bradleythome.githubserach.core.ForApplication
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

/**
 * Created by brad.thome.
 *
 * Picasso Module for image loading
 */

@Module(includes = arrayOf(AppModule::class, NetworkModule::class))
class PicassoModule {

    @Provides
    @ApplicationScope
    fun providePicasso(@ForApplication context: Context, okhttp3Downloader: OkHttp3Downloader): Picasso {
        return Picasso.Builder(context)
                .downloader(okhttp3Downloader)
                .build()
    }

    @Provides
    @ApplicationScope
    fun provideOkHttpDownloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }
}
