package com.example.bradleythome.githubserach.core

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.bradleythome.githubserach.results.ResultsActivityViewModel
import com.example.bradleythome.githubserach.results.fragment.CommitsResultsViewModel
import com.example.bradleythome.githubserach.results.fragment.IssuesResultsViewModel
import com.example.bradleythome.githubserach.results.fragment.RepoResultsViewModel
import com.example.bradleythome.githubserach.results.fragment.UsersResultsViewModel
import com.example.bradleythome.githubserach.results.sort.SortOrderViewModel
import com.example.bradleythome.githubserach.results.webview.WebViewSelectViewModel
import com.example.bradleythome.githubserach.results.webview.WebViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by brad.thome.
 *
 * ViewModelModule all view models should be manually addded here
 */

@Module
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: CustomViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ResultsActivityViewModel::class)
    abstract fun bindResultsViewModel(viewModel: ResultsActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WebViewViewModel::class)
    abstract fun bindWebViewViewModel(viewModel: WebViewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepoResultsViewModel::class)
    abstract fun bindRepoResultsViewModel(viewModel: RepoResultsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UsersResultsViewModel::class)
    abstract fun bindUsersResultsViewModel(viewModel: UsersResultsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IssuesResultsViewModel::class)
    abstract fun bindIssuesResultsViewModel(viewModel: IssuesResultsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommitsResultsViewModel::class)
    abstract fun bindCommitsResultsViewModel(viewModel: CommitsResultsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WebViewSelectViewModel::class)
    abstract fun bindWebViewSelectViewModel(viewModel: WebViewSelectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SortOrderViewModel::class)
    abstract fun bindSortOrderViewModel(viewModel: SortOrderViewModel): ViewModel


}

