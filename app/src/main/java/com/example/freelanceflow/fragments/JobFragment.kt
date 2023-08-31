package com.example.freelanceflow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freelanceflow.R
import com.example.freelanceflow.activities.MainActivity
import com.example.freelanceflow.adapters.RemoteJobAdapter
import com.example.freelanceflow.databinding.FragmentJobBinding
import com.example.freelanceflow.repository.RemoteJobRepository
import com.example.freelanceflow.viewmodels.RemoteJobViewModel
import com.example.freelanceflow.viewmodels.RemoteJobViewModelFactory

class JobFragment : Fragment(R.layout.fragment_job) {
    lateinit var remoteJobViewModel: RemoteJobViewModel
    lateinit var jobBinding: FragmentJobBinding
    val remoteJobAdapter = RemoteJobAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remoteJobViewModel = (activity as MainActivity).remoteJobViewModel

        //remoteJobViewModel = (activity as MainActivity).remoteJobViewModel
        jobBinding = FragmentJobBinding.bind(view)
       // setUpViewModel()
        setUpRecyclerView()
        ObserveData()

    }

    private fun setUpRecyclerView() {
        jobBinding.rvNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = remoteJobAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
        }

        jobBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text changes
                remoteJobAdapter.getFilter().filter(newText)
                return true
            }
        })
    }

    private fun ObserveData(){
        remoteJobViewModel.remoteJobResult().observe(viewLifecycleOwner, Observer {
            if (it!=null){
                remoteJobAdapter.submitList(it.jobs)
            }
        })
    }


}