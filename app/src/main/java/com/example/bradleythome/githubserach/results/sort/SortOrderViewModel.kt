package com.example.bradleythome.githubserach.results.sort

import android.app.Application
import com.example.bradleythome.githubserach.core.base.BaseViewModel
import com.example.bradleythome.githubserach.network.GithubRepository
import javax.inject.Inject

/**
 * Created by bradley.thome on 10/21/17.
 */
class SortOrderViewModel @Inject constructor(app: Application, val githubRepository: GithubRepository) : BaseViewModel(app) {

    lateinit var baseResultsViewModel: com.example.bradleythome.githubserach.results.fragment.BaseResultsViewModel<*>

    init {

    }

}
