package com.example.hellofileupload

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var url: String
    lateinit var webView: WebView
    lateinit var upload: ValueCallback<Array<Uri>>

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        url = ""
        webView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

        webView.webViewClient = MyWeb()
        webView.webChromeClient = object: WebChromeClient(){
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                super.onShowFileChooser(webView, filePathCallback, fileChooserParams)

                val intent: Intent = fileChooserParams.createIntent()
                upload = filePathCallback

                startActivityForResult(intent, 101)

                return true
            }
        }
    }

    private class MyWeb: WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101) {
            upload.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
        }
    }
}