package com.example.bradleythome.githubserach.results.webview

import android.app.Application
import com.example.bradleythome.githubserach.core.base.BaseViewModel
import com.example.bradleythome.githubserach.extensions.observe
import javax.inject.Inject


class WebViewViewModel @Inject constructor(app: Application) : BaseViewModel(app) {
    val urlObserve = "".observe
    var url by urlObserve
}
