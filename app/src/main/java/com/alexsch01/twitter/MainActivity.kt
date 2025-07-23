package com.alexsch01.twitter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

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

        if (intent.dataString == null) {
            myWebView.loadUrl("https://x.com")
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
