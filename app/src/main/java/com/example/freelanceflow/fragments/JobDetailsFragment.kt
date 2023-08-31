package com.example.freelanceflow.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.freelanceflow.R
import com.example.freelanceflow.api.Job
import com.example.freelanceflow.databinding.FragmentJobDetailsBinding




class JobDetailsFragment : Fragment(R.layout.fragment_job_details) {

    lateinit var jobBinding: FragmentJobDetailsBinding
    lateinit var currentJob: Job
    private val args: JobDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobBinding = FragmentJobDetailsBinding.bind(view)
        currentJob = args.job!!

        setUpWebView()
    }

    private fun setUpWebView() {
        jobBinding.webView.apply {
            webViewClient = WebViewClient()
            currentJob.url?.let {
                loadUrl(it)
            }

        }
    }
}