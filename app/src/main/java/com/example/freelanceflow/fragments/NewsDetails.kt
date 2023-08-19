package com.example.freelanceflow.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.FragmentNewsDetailsBinding
import com.example.freelanceflow.newsapi.Article


class NewsDetails : Fragment(R.layout.fragment_news_details) {
    lateinit var newsBinding: FragmentNewsDetailsBinding
    lateinit var currentArticle: Article
    private val args: NewsDetailsArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsBinding = FragmentNewsDetailsBinding.bind(view)
        currentArticle = args.news!!

        setUpWebView()
    }

    private fun setUpWebView() {
        newsBinding.newsWebView.apply {
            webViewClient = WebViewClient()
            currentArticle.url?.let { loadUrl(it) }

        }
    }
}