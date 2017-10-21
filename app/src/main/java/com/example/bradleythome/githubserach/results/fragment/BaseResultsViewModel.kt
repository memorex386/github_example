package com.example.bradleythome.githubserach.results.fragment

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.core.BaseActivity
import com.example.bradleythome.githubserach.core.BaseViewModel
import com.example.bradleythome.githubserach.databinding.OrderDialogBinding
import com.example.bradleythome.githubserach.databinding.SortDialogBinding
import com.example.bradleythome.githubserach.models.*
import com.example.bradleythome.githubserach.network.GithubRepository
import com.example.bradleythome.githubserach.results.ResultsActivityViewModel
import com.example.bradleythome.githubserach.results.sort.SortOrderViewModel
import com.example.bradleythome.githubserach.search.*
import com.example.bradleythome.githubserach.uitl.LiveDataAction
import com.example.bradleythome.githubserach.uitl.subscribe
import javax.inject.Inject

/**
 * Created by bradley.thome on 10/17/17.
 */


abstract class BaseResultsViewModel<T : ResultsItem> constructor(val searchEnum: SearchEnum, app: Application, githubRepository: GithubRepository) : BaseViewModel(app) {


    val currentPage = ObservableField<Int>(1)
    val totalPages = ObservableField<Int>(1)
    val sort = ObservableField<Pair<String, String>>(Pair("", "Best Match"))
    val order = ObservableField<Order>(Order.DESC)

    var lastSearch: SearchOptions? = null
    var searchingFor: SearchOptions? = null

    val scrollUpRequested = LiveDataAction()

    val searchViewContainer: SearchViewContainer<*>

    var dialog: AlertDialog? = null

    init {
        currentPage.subscribe(compositeDisposable) {
            search()
        }
        sort.subscribe(compositeDisposable) {
            search()
        }
        order.subscribe(compositeDisposable) {
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
                querySearch.subscribe(compositeDisposable) { queryRequest ->

                    search()
                }
                querySearch.get()?.let {
                    if (it.isNotBlank()) {
                        search()
                    }
                }
            }
        }


    fun search() {

        fun clear() {
            currentPage.set(1)
            totalPages.set(1)
            lastSearch = null
            searchingFor = null
            searchViewContainer.hasResults.set(false)
            searchViewContainer.noResultsText.set(searchEnum.startSearch(app))
        }

        resultsActivityViewModel?.querySearch?.get()?.let {
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
        dialog?.apply {
            if (isShowing) dismiss()
        }
    }

    fun chooseOrder(activity: BaseActivity, sortViewModel: SortOrderViewModel) {

        dialog(activity, sortViewModel, R.layout.order_dialog) {
            DataBindingUtil.inflate<OrderDialogBinding>(LayoutInflater.from(activity), R.layout.order_dialog, null, false).apply {
                viewModel = sortViewModel
            }
        }
    }

    fun dialog(activity: BaseActivity, sortViewModel: SortOrderViewModel, @LayoutRes layout: Int, binding: () -> ViewDataBinding) {
        sortViewModel.baseResultsViewModel = this

        dialog = activity.dialogSelect(binding)

    }


    fun chooseSort(activity: BaseActivity, sortViewModel: SortOrderViewModel) {

        dialog(activity, sortViewModel, R.layout.order_dialog) {
            DataBindingUtil.inflate<SortDialogBinding>(LayoutInflater.from(activity), R.layout.sort_dialog, null, false).apply {
                viewModel = sortViewModel
            }
        }
    }

    val searchOptions: SearchOptions
        get() = SearchOptions(searchEnum, resultsActivityViewModel?.querySearch?.get() ?: "", currentPage.get(), sort.get(), order.get())
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
