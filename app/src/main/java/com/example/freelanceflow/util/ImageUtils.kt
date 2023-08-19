package com.example.freelanceflow.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import java.io.IOException


object ImageUtils {
    fun loadImage(
        context: Context,
        uri: Uri,
        imageView: ImageView
    ) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}