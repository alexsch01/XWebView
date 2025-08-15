package com.alexsch01.twitter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myWebView = findViewById(R.id.webView)
        myWebView.overScrollMode = WebView.OVER_SCROLL_NEVER
        myWebView.isVerticalScrollBarEnabled = false
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true

        val baseUrl = "https://x.com"

        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val website = request?.url.toString()

                if (website.startsWith(baseUrl)) {
                    return false
                }

                view?.context?.startActivity(Intent(
                    Intent.ACTION_VIEW,
                    website.toUri()
                ))
                return true
            }
        }

        // Workaround for fullscreen videos
        myWebView.webChromeClient = object : WebChromeClient() {
            private val frameLayout: FrameLayout = findViewById(R.id.frameLayout)
            private val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                // get into proper fullscreen mode
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                insetsController.hide(WindowInsetsCompat.Type.systemBars())

                frameLayout.addView(view, 1)
            }

            override fun onHideCustomView() {
                frameLayout.removeViewAt(1)

                // get out of proper fullscreen mode
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }

        if (intent.dataString == null) {
            myWebView.loadUrl(baseUrl)
        } else {
            myWebView.loadUrl(intent.dataString!!)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.dataString != null) {
            myWebView.loadUrl(intent.dataString!!)
        }
    }

    override fun onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
