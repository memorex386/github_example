package com.example.bradleythome.githubserach.models

import com.squareup.moshi.Json


/**
 * Created by bradley.thome on 10/19/17.
 */
class UserItem : ResultsItem() {

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
    @Json(name = "followers_url")
    var followersUrl: String? = null
    @Json(name = "subscriptions_url")
    var subscriptionsUrl: String? = null
    @Json(name = "organizations_url")
    var organizationsUrl: String? = null
    @Json(name = "repos_url")
    var reposUrl: String? = null
    @Json(name = "received_events_url")
    var receivedEventsUrl: String? = null
    @Json(name = "type")
    var type: String? = null
    @Json(name = "score")
    var score: Float = 0.toFloat()

}

class Users : Results<UserItem>() {

    @Json(name = "items")
    override var items: List<UserItem>? = null

}
