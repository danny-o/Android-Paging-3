package com.digitalskies.androidpaging3.ui.newsArticleActivity

import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.digitalskies.androidpaging3.databinding.ActivityNewsArticleBinding

const val ARTICLE_URL="com.digitalskies.androidpaging3.article_url"

class NewsArticleActivity:AppCompatActivity() {
    lateinit var binding: ActivityNewsArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNewsArticleBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val articleUrl=intent.getStringExtra(ARTICLE_URL)



        binding.wbArticleContent.webViewClient=object: WebViewClient(){
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }
        }

        binding.wbArticleContent.settings.javaScriptEnabled=true

        binding.wbArticleContent.webChromeClient=object:WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressBarHorizontal.progress=newProgress
                if(newProgress==100){
                    binding.progressBarHorizontal.visibility= View.GONE
                }
            }
        }

        binding.wbArticleContent.loadUrl(articleUrl.toString())




    }
}