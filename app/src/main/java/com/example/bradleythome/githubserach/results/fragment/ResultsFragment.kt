package com.example.bradleythome.githubserach.results.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.core.base.BaseFragment
import com.example.bradleythome.githubserach.databinding.ResultsFragmentBinding
import com.example.bradleythome.githubserach.databinding.WebviewDialogBinding
import com.example.bradleythome.githubserach.models.*
import com.example.bradleythome.githubserach.results.ResultsActivityViewModel
import com.example.bradleythome.githubserach.results.webview.WebViewActivity
import com.example.bradleythome.githubserach.results.webview.WebViewSelectViewModel
import com.example.bradleythome.githubserach.search.SearchViewContainer
import kotlinx.android.synthetic.main.results_fragment.*


sealed class BaseResultsFragment<T : ResultsItem, VIEW_MODEL : BaseResultsViewModel<T>> : BaseFragment<VIEW_MODEL, ResultsFragmentBinding>() {

    override val layoutRes: Int
        get() = R.layout.results_fragment

    val webViewSelectViewModel by lazy { ViewModelProviders.of(activity, viewModelFactory).get(WebViewSelectViewModel::class.java) }

    lateinit var searchViewContainer: SearchViewContainer<T>

    val publicViewModel
        get() = viewModel

    lateinit var resultsFragmentInterface: ResultsFragmentInterface

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        resultsFragmentInterface = context as ResultsFragmentInterface
    }


    private var dialog: AlertDialog? = null


    override fun preDataBinded(savedInstanceState: Bundle?) {
        super.preDataBinded(savedInstanceState)

        viewModel.resultsActivityViewModel = resultsFragmentInterface.activityViewModel()

        webViewSelectViewModel.customTab.onChanged {
            dialog?.apply {
                dismiss()
            }
            val builder = CustomTabsIntent.Builder();
            val customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(it));

        }

        webViewSelectViewModel.webview.onChanged {
            dialog?.apply {
                dismiss()
            }
            startActivity(WebViewActivity.newIntent(context, it))
        }

        fun noUrl() {
            //TODO handle no url here
        }


        viewModel.searchViewContainer.itemClickedAction.onChanged {
            it.htmlUrl?.let { url ->
                dialog = context.dialogSelect {
                    DataBindingUtil.inflate<WebviewDialogBinding>(LayoutInflater.from(activity), R.layout.webview_dialog, null, false).apply {
                        webViewSelectViewModel.url = url
                        viewModel = webViewSelectViewModel
                    }
                }
            } ?: noUrl()
        }

        viewModel.scrollUpRequested.onChanged {
            recycler_view.scrollToPosition(0)
        }

    }

    override fun onCreateDataBinded(savedInstanceState: Bundle?) {
        viewModel.lifecycleOwner = this@BaseResultsFragment
        binding.viewModel = viewModel
    }

    interface ResultsFragmentInterface {
        fun activityViewModel(): ResultsActivityViewModel
    }
}

class RepoFragment() : BaseResultsFragment<GithubRepo, RepoResultsViewModel>() {

    override val viewModelClass: Class<RepoResultsViewModel>
        get() = RepoResultsViewModel::class.java
}

class UsersFragment() : BaseResultsFragment<UserItem, UsersResultsViewModel>() {
    override val viewModelClass: Class<UsersResultsViewModel>
        get() = UsersResultsViewModel::class.java
}

class CommitsFragment() : BaseResultsFragment<CommitItem, CommitsResultsViewModel>() {
    override val viewModelClass: Class<CommitsResultsViewModel>
        get() = CommitsResultsViewModel::class.java
}

class IssuesFragment() : BaseResultsFragment<IssueItem, IssuesResultsViewModel>() {
    override val viewModelClass: Class<IssuesResultsViewModel>
        get() = IssuesResultsViewModel::class.java
}
