package com.example.bradleythome.githubserach.network

import com.example.bradleythome.githubserach.models.Commits
import com.example.bradleythome.githubserach.models.Issues
import com.example.bradleythome.githubserach.models.Repositories
import com.example.bradleythome.githubserach.models.Users
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by bradley.thome on 10/17/17.
 */

interface Api {

    @GET("search/repositories")
    @Headers("Accept: application/vnd.github.cloak-preview")
    fun getGithubRepo(
            @Query("q") query: String,
            @Query("page") pageNumber: Int,
            @Query("sort") sort: String? = null,
            @Query("order") order: String? = null)
            : Single<Repositories>

    @GET("search/issues")
    @Headers("Accept: application/vnd.github.cloak-preview")
    fun getIssueitem(
            @Query("q") query: String,
            @Query("page") pageNumber: Int,
            @Query("sort") sort: String? = null,
            @Query("order") order: String? = null)
            : Single<Issues>

    @GET("search/users")
    @Headers("Accept: application/vnd.github.cloak-preview")
    fun getUserItem(
            @Query("q") query: String,
            @Query("page") pageNumber: Int,
            @Query("sort") sort: String? = null,
            @Query("order") order: String? = null)
            : Single<Users>

    @GET("search/commits")
    @Headers("Accept: application/vnd.github.cloak-preview")
    fun getCommit(
            @Query("q") query: String,
            @Query("page") pageNumber: Int,
            @Query("sort") sort: String? = null,
            @Query("order") order: String? = null)
            : Single<Commits>
}

