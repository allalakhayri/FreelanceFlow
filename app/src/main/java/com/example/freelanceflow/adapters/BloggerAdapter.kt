package com.example.freelanceflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.TechNewsItemsBinding
import com.example.freelanceflow.fragments.NewsDetails
import com.example.freelanceflow.fragments.TechNewsFragmentDirections
import com.example.freelanceflow.newsapi.Article
import com.squareup.picasso.Picasso




class BloggerAdapter: ListAdapter<Article, BloggerAdapter.BloggerViewHolder>(BloggerCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloggerViewHolder {
        return BloggerViewHolder(
            (LayoutInflater.from(parent.context).inflate(R.layout.tech_news_items, parent, false))
        )
    }

    override fun onBindViewHolder(holder: BloggerViewHolder, position: Int) {
        getItem(position).let {
            holder.apply {
                title.text = it.title
                publisher.text = it.publishedAt
                description.text = it.description
                // Glide.with(BlogsFragment()).load(it.urlToImage).error(R.drawable.defaultimage).into(image)
                Picasso.get().load(it.urlToImage).error(R.drawable.defaultimage).into(image)
                itemView.setOnClickListener {
                    val direction=TechNewsFragmentDirections.actionTechNewsFragmentToNewsDetails2(getItem(position))
                    it.findNavController().navigate(direction)

                }
            }

        }
    }

    inner class BloggerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val techNewsItemsBinding = TechNewsItemsBinding.bind(itemView)
        val title = techNewsItemsBinding.tvTitle
        val publisher = techNewsItemsBinding.tvPublisher
        val description = techNewsItemsBinding.tvDescription
        val image = techNewsItemsBinding.ivArticleImage
    }
}

