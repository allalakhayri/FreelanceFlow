package com.example.freelanceflow.util


import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.freelanceflow.repository.TechNewsRepository
import com.example.freelanceflow.viewmodels.TechNewsViewModel
import com.example.freelanceflow.viewmodels.TechNewsViewModelFactory

object ViewModelSetupUtil {
    fun setupTechNewsViewModel(activity: FragmentActivity): TechNewsViewModel {
        val techNewsRepository = TechNewsRepository()
        val remoteViewModelFactory = TechNewsViewModelFactory(activity.application, techNewsRepository)
        return ViewModelProvider(activity, remoteViewModelFactory).get(TechNewsViewModel::class.java)
    }
}

