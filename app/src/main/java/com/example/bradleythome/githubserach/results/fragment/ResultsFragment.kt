package com.example.bradleythome.githubserach.results.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.core.BaseFragment
import com.example.bradleythome.githubserach.databinding.ResultsFragmentBinding
import com.example.bradleythome.githubserach.databinding.WebviewDialogBinding
import com.example.bradleythome.githubserach.models.*
import com.example.bradleythome.githubserach.results.ResultsActivityViewModel
import com.example.bradleythome.githubserach.results.webview.WebViewActivity
import com.example.bradleythome.githubserach.results.webview.WebViewSelectViewModel
import com.example.bradleythome.githubserach.search.SearchViewContainer
import kotlinx.android.synthetic.main.results_fragment.*
import javax.inject.Inject


abstract class BaseResultsFragment<T : ResultsItem> : BaseFragment() {


    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: BaseResultsViewModel<T> by lazy { ViewModelProviders.of(activity, viewModelFactory).get(getClazz()) }
    val webViewSelectViewModel by lazy { ViewModelProviders.of(activity, viewModelFactory).get(WebViewSelectViewModel::class.java) }
    lateinit var binding: ResultsFragmentBinding

    lateinit var searchViewContainer: SearchViewContainer<T>

    lateinit var resultsFragmentInterface: ResultsFragmentInterface

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        resultsFragmentInterface = context as ResultsFragmentInterface
    }


    private var dialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.resultsActivityViewModel = resultsFragmentInterface.activityViewModel()

        webViewSelectViewModel.customTab.observe(this) {
            dialog?.apply {
                dismiss()
            }
            val builder = CustomTabsIntent.Builder();
            val customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(it));

        }

        webViewSelectViewModel.webview.observe(this) {
            dialog?.apply {
                dismiss()
            }
            startActivity(WebViewActivity.newIntent(context, it))
        }

        fun noUrl() {
            //TODO handle no url here
        }

        viewModel.searchViewContainer.itemClickedAction.observe(this) {
            it.htmlUrl?.let { url ->
                dialog = context.dialogSelect {
                    DataBindingUtil.inflate<WebviewDialogBinding>(LayoutInflater.from(activity), R.layout.webview_dialog, null, false).apply {
                        webViewSelectViewModel.url.set(url)
                        viewModel = webViewSelectViewModel
                    }
                }
            } ?: noUrl()
        }


        val binding = DataBindingUtil.inflate<ResultsFragmentBinding>(inflater, R.layout.results_fragment, container, false)
        val view = binding.root
        binding.viewModel = viewModel

        viewModel.scrollUpRequested.observe(this) {
            recycler_view.scrollToPosition(0)
        }

        with(binding) {

        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface ResultsFragmentInterface {
        fun activityViewModel(): ResultsActivityViewModel
    }

    abstract fun getClazz(): Class<out BaseResultsViewModel<T>>


}

class RepoFragment() : BaseResultsFragment<GithubRepo>() {
    override fun getClazz(): Class<out BaseResultsViewModel<GithubRepo>> = RepoResultsViewModel::class.java
}

class UsersFragment() : BaseResultsFragment<UserItem>() {
    override fun getClazz(): Class<out BaseResultsViewModel<UserItem>> = UsersResultsViewModel::class.java
}

class CommitsFragment() : BaseResultsFragment<CommitItem>() {
    override fun getClazz(): Class<out BaseResultsViewModel<CommitItem>> = CommitsResultsViewModel::class.java
}

class IssuesFragment() : BaseResultsFragment<IssueItem>() {
    override fun getClazz(): Class<out BaseResultsViewModel<IssueItem>> = IssuesResultsViewModel::class.java
}
