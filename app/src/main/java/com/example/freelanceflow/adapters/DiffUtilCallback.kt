package com.example.freelanceflow.adapters

import androidx.recyclerview.widget.DiffUtil

import com.example.freelanceflow.api.Job


class DiffUtilCallback: DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.url == newItem.url

    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}