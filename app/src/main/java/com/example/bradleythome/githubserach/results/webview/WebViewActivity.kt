package com.example.bradleythome.githubserach.results.webview

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.core.base.BaseLifecycleActivity
import com.example.bradleythome.githubserach.databinding.WebViewActivityBinding
import kotlinx.android.synthetic.main.web_view_activity.*
import javax.inject.Inject

/**
 * Created by bradley.thome on 10/19/17.
 */
class WebViewActivity : BaseLifecycleActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(WebViewViewModel::class.java) }


    companion object {
        val ARG_URL = "ARG_URL"

        fun newIntent(context: Context, url: String): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ARG_URL, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.url = intent.getStringExtra(ARG_URL)

        val binding = DataBindingUtil.setContentView<WebViewActivityBinding>(this, R.layout.web_view_activity)

        web_view.setWebViewClient(ExternalWebViewClient())
        web_view.setWebChromeClient(ExternalWebChromeClient())

        binding.viewModel = viewModel
    }

    private inner class ExternalWebViewClient : WebViewClient() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return shouldOverrideUrlLoading(view, request.url.toString())
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            web_view.loadUrl(url)
            return true
        }
    }

    private inner class ExternalWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            if (!TextUtils.isEmpty(title)) {
                setTitle(title)
            }
        }
    }
}
