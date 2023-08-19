package com.example.freelanceflow.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.freelanceflow.R
import com.example.freelanceflow.model.BasicInfo
import com.example.freelanceflow.util.ImageUtils
import com.example.freelanceflow.util.PermissionUtils

class BasicInfoEditActivity : EditBaseActivity<BasicInfo>() {

    companion object {
        const val KEY_BASIC_INFO = "basic_info"
        private const val REQ_CODE_PICK_IMAGE = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let { showImage(it) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_basic_info_edit
    }

    override fun setupUIForCreate() { }

    override fun setupUIForEdit(data: BasicInfo) {
        findViewById<EditText>(R.id.basic_info_edit_name).setText(data.name)
        findViewById<EditText>(R.id.basic_info_edit_email).setText(data.email)

        data.imageUri?.let { showImage(it) }

        findViewById<View>(R.id.basic_info_edit_image_layout).setOnClickListener {
            if (!PermissionUtils.checkPermission(this@BasicInfoEditActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PermissionUtils.requestReadExternalStoragePermission(this@BasicInfoEditActivity)
            } else {
                pickImage()
            }
        }
    }

    override fun saveAndExit(data: BasicInfo?) {
        val newData = data ?: BasicInfo()

        newData.name = findViewById<EditText>(R.id.basic_info_edit_name).text.toString()
        newData.email = findViewById<EditText>(R.id.basic_info_edit_email).text.toString()
        newData.imageUri = findViewById<ImageView>(R.id.basic_info_edit_image).tag as Uri?

        val resultIntent = Intent()
        resultIntent.putExtra(KEY_BASIC_INFO, newData)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun initializeData(): BasicInfo? {
        return intent.getParcelableExtra(KEY_BASIC_INFO)
    }

    private fun showImage(imageUri: Uri) {
        val imageView = findViewById<ImageView>(R.id.basic_info_edit_image)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        imageView.tag = imageUri
        ImageUtils.loadImage(this, imageUri, imageView)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQ_CODE_PICK_IMAGE)
    }
}
