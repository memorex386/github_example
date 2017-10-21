package com.example.bradleythome.githubserach.models

import com.squareup.moshi.Json


/**
 * Created by bradley.thome on 10/18/17.
 */

class Issues : Results<IssueItem>() {

    @Json(name = "items")
    override var items: List<IssueItem>? = null

}


class IssueItem : ResultsItem() {

    @Json(name = "url")
    var url: String? = null
    @Json(name = "repository_url")
    var repositoryUrl: String? = null
    @Json(name = "labels_url")
    var labelsUrl: String? = null
    @Json(name = "comments_url")
    var commentsUrl: String? = null
    @Json(name = "events_url")
    var eventsUrl: String? = null
    @Json(name = "id")
    var id: Int = 0
    @Json(name = "number")
    var number: Int = 0
    @Json(name = "title")
    var title: String? = null
    @Json(name = "user")
    var user: User? = null
    @Json(name = "labels")
    var labels: List<Label>? = null
    @Json(name = "state")
    var state: String? = null
    @Json(name = "assignee")
    var assignee: Any? = null
    @Json(name = "milestone")
    var milestone: Any? = null
    @Json(name = "comments")
    var comments: Int = 0
    @Json(name = "created_at")
    var createdAt: String? = null
    @Json(name = "updated_at")
    var updatedAt: String? = null
    @Json(name = "closed_at")
    var closedAt: Any? = null
    @Json(name = "pull_request")
    var pullRequest: PullRequest? = null
    @Json(name = "body")
    var body: String? = null
    @Json(name = "score")
    var score: Float = 0.toFloat()

}

class Label {

    @Json(name = "url")
    var url: String? = null
    @Json(name = "name")
    var name: String? = null
    @Json(name = "color")
    var color: String? = null

}

class PullRequest {

    @Json(name = "html_url")
    var htmlUrl: Any? = null
    @Json(name = "diff_url")
    var diffUrl: Any? = null
    @Json(name = "patch_url")
    var patchUrl: Any? = null

}

class User {

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

}
