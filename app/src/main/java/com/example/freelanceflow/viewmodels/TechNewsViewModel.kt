package com.example.freelanceflow.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.freelanceflow.repository.TechNewsRepository


class TechNewsViewModel(val app: Application, private val repository: TechNewsRepository): AndroidViewModel(app) {

   // fun remoteJobResult() = repository.remoteJobResult()
    fun getTechnologyNews() = repository.newsResult()
}