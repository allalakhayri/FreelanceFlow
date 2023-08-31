package com.example.freelanceflow.repository

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.freelanceflow.users.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val storageReference = FirebaseStorage.getInstance().getReference("post_images")
    private val auth = FirebaseAuth.getInstance()

    suspend fun addPost(imageUri: Uri, title:String, readTime :Int, postDescription: String, contentResolver: ContentResolver): Boolean {
        return try {
            // Upload image to Firebase Storage
            val fileReference = storageReference.child("${System.currentTimeMillis()}" + "." + getFileExtension(imageUri, contentResolver))
            val uploadTask = fileReference.putFile(imageUri).await()
            val imageUrl = uploadTask.storage.downloadUrl.await().toString()

            // Create post object with necessary data
            val reference = firebaseDatabase.getReference("Post").push()
            val postKey = reference.key

            val post = Post(
                postDescription,
                title,
                readTime,

                auth.currentUser?.uid,
                auth.currentUser?.email,
                imageUrl,
                postKey
            )

            // Push the post data to Firebase Database
            postKey?.let {
                reference.setValue(post)
            }

            true // Return success status
        } catch (e: Exception) {
            false // Return failure status
        }
    }

    private fun getFileExtension(imageUri: Uri, contentResolver: ContentResolver): String? {
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(imageUri))
    }
}
