package com.example.bradleythome.githubserach.results.webview

import android.app.Application
import android.databinding.ObservableField
import com.example.bradleythome.githubserach.core.base.BaseViewModel
import com.example.bradleythome.githubserach.uitl.ActionItem
import javax.inject.Inject

class WebViewSelectViewModel @Inject constructor(app: Application) : BaseViewModel(app) {
    val url = ObservableField<String>()
    val webview = ActionItem<String>()
    val customTab = ActionItem<String>()

    fun webViewClicked() {
        webview.actionOccurred(url.get())
    }

    fun customTabClicked() {
        customTab.actionOccurred(url.get())
    }
}
