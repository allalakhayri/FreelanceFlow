package com.example.freelanceflow.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freelanceflow.repository.TechNewsRepository

class TechNewsViewModelFactory(val app: Application, private val repository: TechNewsRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TechNewsViewModel(app, repository) as T
    }
}