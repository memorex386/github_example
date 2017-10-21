package com.example.bradleythome.githubserach.models

import com.squareup.moshi.Json


/**
 * Created by bradley.thome on 10/18/17.
 */


class Commits : Results<CommitItem>() {
    @Json(name = "items")
    override var items: List<CommitItem>? = null
}


class GithubAuthor {

    @Json(name = "login")
    var login: String? = null
    @Json(name = "id")
    var id: Int = 0
    @Json(name = "avatar_url")
    var avatarUrl: String? = null
    @Json(name = "gravatar_id")
    var gravatarId: String? = null
    @Json(name = "url")
    var url: String? = null
    @Json(name = "html_url")
    var htmlUrl: String? = null
    @Json(name = "followers_url")
    var followersUrl: String? = null
    @Json(name = "following_url")
    var followingUrl: String? = null
    @Json(name = "gists_url")
    var gistsUrl: String? = null
    @Json(name = "starred_url")
    var starredUrl: String? = null
    @Json(name = "subscriptions_url")
    var subscriptionsUrl: String? = null
    @Json(name = "organizations_url")
    var organizationsUrl: String? = null
    @Json(name = "repos_url")
    var reposUrl: String? = null
    @Json(name = "events_url")
    var eventsUrl: String? = null
    @Json(name = "received_events_url")
    var receivedEventsUrl: String? = null
    @Json(name = "type")
    var type: String? = null
    @Json(name = "site_admin")
    var siteAdmin: Boolean = false

}

class CommitAuthor {

    @Json(name = "date")
    var date: String? = null
    @Json(name = "name")
    var name: String? = null
    @Json(name = "email")
    var email: String? = null

}

class Commit {

    @Json(name = "url")
    var url: String? = null
    @Json(name = "author")
    var author: CommitAuthor? = null
    @Json(name = "committer")
    var committer: Committer? = null
    @Json(name = "message")
    var message: String? = null
    @Json(name = "tree")
    var tree: Tree? = null
    @Json(name = "comment_count")
    var commentCount: Int = 0

}

class RepoCommitter {

    @Json(name = "login")
    var login: String? = null
    @Json(name = "id")
    var id: Int = 0
    @Json(name = "avatar_url")
    var avatarUrl: String? = null
    @Json(name = "gravatar_id")
    var gravatarId: String? = null
    @Json(name = "url")
    var url: String? = null
    @Json(name = "html_url")
    var htmlUrl: String? = null
    @Json(name = "followers_url")
    var followersUrl: String? = null
    @Json(name = "following_url")
    var followingUrl: String? = null
    @Json(name = "gists_url")
    var gistsUrl: String? = null
    @Json(name = "starred_url")
    var starredUrl: String? = null
    @Json(name = "subscriptions_url")
    var subscriptionsUrl: String? = null
    @Json(name = "organizations_url")
    var organizationsUrl: String? = null
    @Json(name = "repos_url")
    var reposUrl: String? = null
    @Json(name = "events_url")
    var eventsUrl: String? = null
    @Json(name = "received_events_url")
    var receivedEventsUrl: String? = null
    @Json(name = "type")
    var type: String? = null
    @Json(name = "site_admin")
    var siteAdmin: Boolean = false

}

class Committer {

    @Json(name = "date")
    var date: String? = null
    @Json(name = "name")
    var name: String? = null
    @Json(name = "email")
    var email: String? = null

}

class CommitItem : ResultsItem() {

    @Json(name = "url")
    var url: String? = null
    @Json(name = "sha")
    var sha: String? = null
    @Json(name = "comments_url")
    var commentsUrl: String? = null
    @Json(name = "commit")
    var commit: Commit? = null
    @Json(name = "author")
    var author: GithubAuthor? = null
    @Json(name = "committer")
    var committer: RepoCommitter? = null
    @Json(name = "parents")
    var parents: List<Parent>? = null
    @Json(name = "repository")
    var repository: GithubRepo? = null
    @Json(name = "score")
    var score: Float = 0.toFloat()

}

class Parent {

    @Json(name = "url")
    var url: String? = null
    @Json(name = "html_url")
    var htmlUrl: String? = null
    @Json(name = "sha")
    var sha: String? = null

}

class Tree {

    @Json(name = "url")
    var url: String? = null
    @Json(name = "sha")
    var sha: String? = null

}
