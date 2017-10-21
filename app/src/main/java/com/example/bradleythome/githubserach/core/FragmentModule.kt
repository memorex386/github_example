package com.example.bradleythome.githubserach.core

import com.example.bradleythome.githubserach.results.fragment.CommitsFragment
import com.example.bradleythome.githubserach.results.fragment.IssuesFragment
import com.example.bradleythome.githubserach.results.fragment.RepoFragment
import com.example.bradleythome.githubserach.results.fragment.UsersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentModule {
    @ContributesAndroidInjector(modules = arrayOf(ViewModelModule::class))
    abstract fun contributeRepoFragment(): RepoFragment

    @ContributesAndroidInjector(modules = arrayOf(ViewModelModule::class))
    abstract fun contributeIssuesFragment(): IssuesFragment

    @ContributesAndroidInjector(modules = arrayOf(ViewModelModule::class))
    abstract fun contributeCommitsFragment(): CommitsFragment

    @ContributesAndroidInjector(modules = arrayOf(ViewModelModule::class))
    abstract fun contributeUsersFragment(): UsersFragment
}
