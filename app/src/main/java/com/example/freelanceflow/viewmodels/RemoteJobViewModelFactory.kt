package com.example.freelanceflow.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freelanceflow.repository.RemoteJobRepository

class RemoteJobViewModelFactory(val app: Application, private val repository: RemoteJobRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RemoteJobViewModel(app, repository) as T
    }
}