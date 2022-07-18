package com.dldmswo1209.webbrowser

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dldmswo1209.webbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var mBinding : ActivityMainBinding? = null
    val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        }else{
            super.onBackPressed()
        }

        super.onBackPressed()

    }
    private fun initViews(){
        binding.webView.apply {
            webViewClient = WebViewClient() // 이 코드를 작성하지 않으면 디폴트 브라우저를 자동으로 실행시켜버림
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true // webView 에서 자바 스크립트를 사용하기 위해서
            loadUrl(DEFAULT_URL)
        }
    }
    private fun bindViews(){
        binding.addressBar.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                val loadingUrl = v.text.toString()
                if(URLUtil.isNetworkUrl(loadingUrl))
                    binding.webView.loadUrl(loadingUrl)
                else
                    binding.webView.loadUrl("http://$loadingUrl")
            }
            return@setOnEditorActionListener false
        }
        binding.goBackButton.setOnClickListener {
            binding.webView.goBack()
        }
        binding.goForwardButton.setOnClickListener {
            binding.webView.goForward()
        }
        binding.goHomeButton.setOnClickListener {
            binding.webView.loadUrl(DEFAULT_URL)
        }
        binding.refreshLayout.setOnRefreshListener {
            binding.webView.reload()
        }
    }

    inner class WebViewClient: android.webkit.WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            binding.progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            binding.refreshLayout.isRefreshing = false
            binding.progressBar.hide()
            binding.goBackButton.isEnabled = binding.webView.canGoBack()
            binding.goForwardButton.isEnabled = binding.webView.canGoForward()
            binding.addressBar.setText(url)
        }
    }

    inner class WebChromeClient: android.webkit.WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            binding.progressBar.progress = newProgress
        }
    }

    companion object{
        private const val DEFAULT_URL = "http://www.google.com"
    }
}