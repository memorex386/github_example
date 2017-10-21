package com.example.bradleythome.githubserach.results.webview

import android.app.Application
import android.databinding.ObservableField
import com.example.bradleythome.githubserach.core.BaseViewModel
import javax.inject.Inject


class WebViewViewModel @Inject constructor(app: Application) : BaseViewModel(app) {
    val url = ObservableField<String>()
}
