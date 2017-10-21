package com.example.bradleythome.githubserach.core

import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by bradley.thome on 10/16/17.
 */
/**
 * An [android.app.Application] that injects its members and can be used to inject [ ]s, [framework fragments][android.app.Fragment], [ ], [android.app.Service]s, [ ]s, and [android.content.ContentProvider]s attached to it.
 */
abstract class DaggerApplication : dagger.android.DaggerApplication(), HasSupportFragmentInjector {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    abstract override fun applicationInjector(): AndroidInjector<out DaggerApplication>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment>? {
        return supportFragmentInjector
    }
}
