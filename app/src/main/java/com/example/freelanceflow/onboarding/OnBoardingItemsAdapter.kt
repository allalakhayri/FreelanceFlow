package com.example.freelanceflow.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.OnboardingContainerItemBinding


class OnBoardingItemsAdapter(private val onBoardingItems: List<OnBoardingItems>): RecyclerView.Adapter<OnBoardingItemsAdapter.OnBoardingItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemsViewHolder {
        return OnBoardingItemsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.onboarding_container_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnBoardingItemsViewHolder, position: Int) {
        val item = onBoardingItems[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }

    inner class OnBoardingItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val onBoardingItemsBinding: OnboardingContainerItemBinding =
            OnboardingContainerItemBinding.bind(view)
        private val imgOnBoarding = onBoardingItemsBinding.imgOnBoarding
        private val title = onBoardingItemsBinding.tvHeading
        private val description = onBoardingItemsBinding.tvDescription

        fun bind(onBoardingItems: OnBoardingItems) {
            imgOnBoarding.setImageResource(onBoardingItems.onBoardingImage)
            title.text = onBoardingItems.title
            description.text = onBoardingItems.description
        }
    }
}