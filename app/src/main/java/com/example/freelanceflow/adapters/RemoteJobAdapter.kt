package com.example.freelanceflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import android.widget.Filter
import android.widget.Filterable
import com.example.freelanceflow.R
import com.example.freelanceflow.api.Job
import com.example.freelanceflow.databinding.JobLayoutAdpaterBinding
import com.example.freelanceflow.fragments.JobFragmentDirections


class RemoteJobAdapter: ListAdapter<Job, RemoteJobAdapter.RemoteJobViewHolder>(DiffUtilCallback()) {
    private var jobListFull: List<Job> = ArrayList()
    init {
        jobListFull = currentList
    }
    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Job>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(jobListFull)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in jobListFull) {
                        if (item.title.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<Job>)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoteJobViewHolder {
        return RemoteJobViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.job_layout_adpater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RemoteJobViewHolder, position: Int) {
        getItem(position).let {
            holder.apply {
                date.text = it.publication_date
                jobTitle.text = it.title
                jobType.text = it.job_type
                jobLocation.text = it.candidate_required_location
                companyName.text = it.company_name
                image.load(it.company_logo_url)

                itemView.setOnClickListener {
                     val direction = JobFragmentDirections.actionJobFragmentToJobDetailsFragment(getItem(position))
                    it.findNavController().navigate(direction)
                }
            }
        }
    }

    inner class RemoteJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rvBinding = JobLayoutAdpaterBinding.bind(itemView)
        val date = rvBinding.tvDate
        val jobTitle = rvBinding.tvJobTitle
        val jobType = rvBinding.tvJobType
        val jobLocation = rvBinding.tvJobLocation
        val image = rvBinding.ivCompanyLogo
        val companyName = rvBinding.tvCompanyName


    }
}
