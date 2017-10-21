package com.example.bradleythome.githubserach.core

import com.example.bradleythome.githubserach.results.ResultsActivity
import com.example.bradleythome.githubserach.results.webview.WebViewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by bradley.thome on 10/16/17.
 */
@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector(modules = arrayOf(ViewModelModule::class))
    abstract fun contributeResultsActivity(): ResultsActivity

    @ContributesAndroidInjector(modules = arrayOf(ViewModelModule::class))
    abstract fun contributeWebViewActivity(): WebViewActivity
}
