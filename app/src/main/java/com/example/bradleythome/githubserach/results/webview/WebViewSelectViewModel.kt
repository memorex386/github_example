package com.example.bradleythome.githubserach.results.webview

import android.app.Application
import com.example.bradleythome.githubserach.core.base.BaseViewModel
import com.example.bradleythome.githubserach.extensions.observe
import com.example.bradleythome.githubserach.uitl.ActionItem
import javax.inject.Inject

class WebViewSelectViewModel @Inject constructor(app: Application) : BaseViewModel(app) {
    val urlObserve = "".observe
    var url by urlObserve
    val webview = ActionItem<String>()
    val customTab = ActionItem<String>()

    fun webViewClicked() {
        webview.actionOccurred(url)
    }

    fun customTabClicked() {
        customTab.actionOccurred(url)
    }
}
