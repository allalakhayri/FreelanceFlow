package com.example.freelanceflow.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.freelanceflow.newsapi.Article

//import com.example.codestack.bloggerapi.BloggerResponse
import com.example.freelanceflow.newsapi.NewsResponse

class BloggerCallBack: DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}