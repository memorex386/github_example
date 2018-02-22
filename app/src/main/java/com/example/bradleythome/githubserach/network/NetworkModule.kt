package com.example.bradleythome.githubserach.network

import android.content.Context
import com.example.bradleythome.githubserach.core.ApplicationScope
import com.example.bradleythome.githubserach.core.ForApplication
import com.example.bradleythome.githubserach.core.moshi
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.File

/**
 * Created by brad.thome.
 *
 * Network Module
 */

@Module
class NetworkModule {

    @ApplicationScope
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.i(message) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return loggingInterceptor
    }

    @ApplicationScope
    @Provides
    fun provideCacheFile(@ForApplication context: Context): File {
        return File(context.cacheDir, "okhttp_cache")
    }

    @ApplicationScope
    @Provides
    fun provideHttpCache(cacheFile: File): Cache {
        val cacheSize = 10L * 1024L * 1024L
        return Cache(cacheFile, cacheSize)
    }

    @ApplicationScope
    @Provides
    fun provideMoshi(): Moshi {
        val moshiBuilder = Moshi.Builder()
        moshiBuilder.add(KotlinJsonAdapterFactory())
        return moshiBuilder.build().apply {
            moshi = this
        }
    }

    @ApplicationScope
    @Provides
    fun provideCacheLengthInterceptor(): Interceptor = Interceptor { chain ->
        val maxAge = 60 * 5 // read from cache for 5 minutes
        val response = chain.proceed(chain.request())
        if (response.isSuccessful)
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build()
        else response
    }

    @ApplicationScope
    @Provides
    fun provideOkhttpClient(loggingInterceptor: HttpLoggingInterceptor, cacheInterceptor: Interceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .cache(cache)
                .build()
    }

    @ApplicationScope
    @Provides
    fun provideRetrofit(oktHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
            .client(oktHttpClient)
            .baseUrl("https://api.github.com") //FIXME
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


    @ApplicationScope
    @Provides
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}
