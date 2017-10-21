package com.example.bradleythome.githubserach.network

import com.example.bradleythome.githubserach.models.Commits
import com.example.bradleythome.githubserach.models.Issues
import com.example.bradleythome.githubserach.models.Repositories
import com.example.bradleythome.githubserach.models.Users
import com.example.bradleythome.githubserach.search.SearchOptions
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bradley.thome on 10/17/17.
 */
class GithubRepository @Inject constructor(private val api: Api) {

    fun getRepos(searchOptions: SearchOptions): Single<Repositories> {
        return api.getGithubRepo(searchOptions.query, searchOptions.page, searchOptions.sort.first.blankIsNull(), searchOptions.orderResult())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

    fun getCommits(searchOptions: SearchOptions): Single<Commits> {
        return api.getCommit(searchOptions.query, searchOptions.page, searchOptions.sort.first.blankIsNull(), searchOptions.orderResult())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

    fun getIssues(searchOptions: SearchOptions): Single<Issues> {
        return api.getIssueitem(searchOptions.query, searchOptions.page, searchOptions.sort.first.blankIsNull(), searchOptions.orderResult())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

    fun getUsers(searchOptions: SearchOptions): Single<Users> {
        return api.getUserItem(searchOptions.query, searchOptions.page, searchOptions.sort.first.blankIsNull(), searchOptions.orderResult())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}

fun String?.blankIsNull(): String? {
    if (this.isNullOrBlank()) return null
    else return this
}

fun SearchOptions.orderResult(): String? {
    if (sort.first.isBlank()) return null
    else return order.order
}
