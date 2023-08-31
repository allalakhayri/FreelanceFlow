package com.example.freelanceflow.viewmodels;


import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freelanceflow.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPostViewModel(private val contentResolver:ContentResolver) : ViewModel() {
    private val repository = PostRepository()

    private val _postAdded = MutableLiveData<Boolean>()
    val postAdded: LiveData<Boolean>
        get() = _postAdded

    fun addPost(imageUri: Uri, title:String, readTime:Int,postDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isSuccess = repository.addPost(imageUri,title, readTime,postDescription,contentResolver)
            _postAdded.postValue(isSuccess)
        }
    }
}