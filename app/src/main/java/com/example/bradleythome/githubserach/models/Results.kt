package com.example.bradleythome.githubserach.models

import com.squareup.moshi.Json

/**
 * Created by bradley.thome on 10/17/17.
 */
abstract class Results<resultsItemType : ResultsItem>() {
    @Json(name = "total_count")
    var totalCount: Int? = null
    @Json(name = "incomplete_results")
    var incompleteResults: Boolean? = null
    @Json(name = "items")
    open var items: List<resultsItemType>? = null
}


abstract class ResultsItem() {
    @Json(name = "html_url")
    var htmlUrl: String? = null
}

