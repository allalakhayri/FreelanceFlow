package com.example.freelanceflow.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    const val REQ_CODE_WRITE_EXTERNAL_STORAGE = 200

    fun checkPermission(@NonNull context: Context, @NonNull permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(@NonNull activity: Activity, permissions: Array<String>, reqCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, reqCode)
    }

    fun requestReadExternalStoragePermission(@NonNull activity: Activity) {
        requestPermissions(activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQ_CODE_WRITE_EXTERNAL_STORAGE)
    }
}
