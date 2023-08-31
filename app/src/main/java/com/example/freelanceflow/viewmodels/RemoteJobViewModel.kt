package com.example.freelanceflow.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.freelanceflow.repository.RemoteJobRepository

class RemoteJobViewModel(val app: Application, private val repository: RemoteJobRepository): AndroidViewModel(app) {

    fun remoteJobResult() = repository.remoteJobResult()
    fun getTechnologyNews() = repository.newsResult()
}