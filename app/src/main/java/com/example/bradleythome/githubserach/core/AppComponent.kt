package com.example.bradleythome.githubserach.core

import com.example.bradleythome.githubserach.network.NetworkModule
import com.example.bradleythome.githubserach.network.PicassoModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@ApplicationScope
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        PicassoModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class)
)
@Singleton
interface AppComponent : AndroidInjector<CustomApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: CustomApplication): Builder

        fun set(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    override fun inject(app: CustomApplication)
}
