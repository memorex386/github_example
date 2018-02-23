package com.example.bradleythome.githubserach.results.fragment

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.core.base.BaseLifecycleActivity
import com.example.bradleythome.githubserach.core.base.BaseViewModel
import com.example.bradleythome.githubserach.databinding.OrderDialogBinding
import com.example.bradleythome.githubserach.databinding.SortDialogBinding
import com.example.bradleythome.githubserach.extensions.ifExists
import com.example.bradleythome.githubserach.extensions.observe
import com.example.bradleythome.githubserach.models.*
import com.example.bradleythome.githubserach.network.GithubRepository
import com.example.bradleythome.githubserach.results.ResultsActivityViewModel
import com.example.bradleythome.githubserach.results.sort.SortOrderViewModel
import com.example.bradleythome.githubserach.search.*
import com.example.bradleythome.githubserach.uitl.Action
import javax.inject.Inject

/**
 * Created by bradley.thome on 10/17/17.
 */


sealed class BaseResultsViewModel<T : ResultsItem> constructor(val searchEnum: SearchEnum, app: Application, githubRepository: GithubRepository) : BaseViewModel(app) {

    val currentPage = 1.observe
    val totalPages = 1.observe
    val sort = Pair("", "Best Match").observe
    val order = Order.DESC.observe

    var lastSearch: SearchOptions? = null
    var searchingFor: SearchOptions? = null

    val scrollUpRequested = Action()

    val searchViewContainer: SearchViewContainer<*>

    var dialog: AlertDialog? = null

    init {
        currentPage.onChanged {
            search()
        }
        sort.onChanged {
            search()
        }
        order.onChanged {
            search()
        }
        searchViewContainer = searchEnum.searchViewContainer(app, this, compositeDisposable, githubRepository)
    }

    fun onBackClicked() {
        if (currentPage.get() <= 1) return
        currentPage.set(currentPage.get() - 1)
    }

    fun onForwardClicked() {
        if (currentPage.get() >= totalPages.get()) return
        currentPage.set(currentPage.get() + 1)
    }

    var resultsActivityViewModel: ResultsActivityViewModel? = null
        set(value) {
            if (field == value) return
            field = value
            value?.apply {
                querySearchObserve.onChanged {
                    search()
                }

                querySearch.ifExists { search() }
            }
        }


    fun search() {

        fun clear() {
            currentPage.set(1)
            totalPages.set(1)
            lastSearch = null
            searchingFor = null
            searchViewContainer.hasResults.set(false)
            searchViewContainer.noResultsText.set(searchEnum.startSearch)
        }

        resultsActivityViewModel?.querySearch?.let {
            if (lastSearch?.equals(searchOptions) ?: false) return
            if (searchingFor == (searchOptions)) return
            searchingFor = searchOptions
            searchViewContainer.search(searchOptions, {
                scrollUpRequested.actionOccurred()
                searchingFor = null
            }, {
                lastSearch = null
                searchingFor = null
            })
        } ?: clear()
    }

    fun closeDialog() {
        dialog?.takeIf { it.isShowing }?.dismiss()
    }

    fun chooseOrder(activity: BaseLifecycleActivity, sortViewModel: SortOrderViewModel) {

        dialog(activity, sortViewModel, R.layout.order_dialog) {
            DataBindingUtil.inflate<OrderDialogBinding>(LayoutInflater.from(activity), R.layout.order_dialog, null, false).apply {
                viewModel = sortViewModel
            }
        }
    }

    fun dialog(activity: BaseLifecycleActivity, sortViewModel: SortOrderViewModel, @LayoutRes layout: Int, binding: () -> ViewDataBinding) {
        sortViewModel.baseResultsViewModel = this

        dialog = activity.dialogSelect(binding)

    }


    fun chooseSort(activity: BaseLifecycleActivity, sortViewModel: SortOrderViewModel) {

        dialog(activity, sortViewModel, R.layout.order_dialog) {
            DataBindingUtil.inflate<SortDialogBinding>(LayoutInflater.from(activity), R.layout.sort_dialog, null, false).apply {
                viewModel = sortViewModel
            }
        }
    }

    val searchOptions: SearchOptions
        get() = SearchOptions(searchEnum, resultsActivityViewModel?.querySearch
                ?: "", currentPage.get(), sort.get(), order.get())
}

fun Context.dialogSelect(binding: () -> ViewDataBinding): AlertDialog {

    val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.myDialog))
    dialogBuilder.setView(binding().root)

    dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

    })

    val dialog = dialogBuilder.create()
    return dialog.apply {
        setOnShowListener {
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this@dialogSelect, R.color.red))
        }
        show()
    }
}

class RepoResultsViewModel @Inject constructor(app: Application, githubRepository: GithubRepository) : BaseResultsViewModel<GithubRepo>(SearchEnum.REPOSITORIES, app, githubRepository) {
}

class UsersResultsViewModel @Inject constructor(app: Application, githubRepository: GithubRepository) : BaseResultsViewModel<UserItem>(SearchEnum.USERS, app, githubRepository) {
}

class CommitsResultsViewModel @Inject constructor(app: Application, githubRepository: GithubRepository) : BaseResultsViewModel<CommitItem>(SearchEnum.COMMITS, app, githubRepository) {
}

class IssuesResultsViewModel @Inject constructor(app: Application, githubRepository: GithubRepository) : BaseResultsViewModel<IssueItem>(SearchEnum.ISSUES, app, githubRepository) {
}
