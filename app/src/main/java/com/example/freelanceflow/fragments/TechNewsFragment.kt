package com.example.freelanceflow.fragments

import android.os.Bundle

import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freelanceflow.R
import com.example.freelanceflow.activities.MainActivity
import com.example.freelanceflow.adapters.BloggerAdapter

import com.example.freelanceflow.databinding.FragmentTechNewsBinding
import com.example.freelanceflow.viewmodels.RemoteJobViewModel


class TechNewsFragment : Fragment(R.layout.fragment_tech_news) {
    val bloggerAdapter = BloggerAdapter()
    lateinit var blogBinding: FragmentTechNewsBinding
    lateinit var remoteJobViewModel: RemoteJobViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remoteJobViewModel = (activity as MainActivity).remoteJobViewModel
        blogBinding = FragmentTechNewsBinding.bind(view)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        blogBinding.rvBlogs.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = bloggerAdapter
            setHasFixedSize(true)
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
        ObserveData()
    }

    private fun ObserveData() {
        remoteJobViewModel.getTechnologyNews().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                bloggerAdapter.submitList(it.articles)
            }
        })
    }
}