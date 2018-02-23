package com.example.bradleythome.githubserach.results

import android.app.Application
import com.example.bradleythome.githubserach.core.base.BaseViewModel
import com.example.bradleythome.githubserach.extensions.Observe
import com.example.bradleythome.githubserach.extensions.observeNull
import com.example.bradleythome.githubserach.network.GithubRepository
import com.example.bradleythome.githubserach.uitl.Action
import javax.inject.Inject


class ResultsActivityViewModel @Inject constructor(app: Application, val githubRepository: GithubRepository) : BaseViewModel(app) {

    val querySearchObserve = "".observeNull
    var querySearch by querySearchObserve

    val adapterObserve = Observe<ResultsPagerAdapter>()
    var adapter by adapterObserve

    val sortAction = Action()
    val orderAction = Action()

    fun sortClicked() {
        sortAction.actionOccurred()
    }

    fun orderClicked() {
        orderAction.actionOccurred()
    }

    fun onQuerySubmitted(string: String?): Boolean {
        querySearch = string
        return true
    }

    fun onQueryChanged(string: String?): Boolean {
        if (string.isNullOrBlank()) querySearch = ""
        return true
    }
}

