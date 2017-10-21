package com.example.bradleythome.githubserach.results

import android.app.Application
import android.databinding.ObservableField
import com.example.bradleythome.githubserach.core.BaseViewModel
import com.example.bradleythome.githubserach.network.GithubRepository
import com.example.bradleythome.githubserach.uitl.LiveDataAction
import javax.inject.Inject


class ResultsActivityViewModel @Inject constructor(app: Application, val githubRepository: GithubRepository) : BaseViewModel(app) {

    val querySearch = ObservableField<String>()

    val adapter = ObservableField<ResultsPagerAdapter>()

    val sortAction = LiveDataAction()
    val orderAction = LiveDataAction()

    fun sortClicked() {
        sortAction.actionOccurred()
    }

    fun orderClicked() {
        orderAction.actionOccurred()
    }

}

