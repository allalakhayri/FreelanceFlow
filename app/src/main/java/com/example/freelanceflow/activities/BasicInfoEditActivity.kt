package com.example.freelanceflow.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.freelanceflow.R
import com.example.freelanceflow.model.BasicInfo
import com.example.freelanceflow.util.ImageUtils
import com.example.freelanceflow.util.PermissionUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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

    override fun setupUIForCreate() {
        findViewById<EditText>(R.id.basic_info_edit_name).setText("")
        findViewById<EditText>(R.id.basic_info_edit_phone).setText("")
        findViewById<EditText>(R.id.basic_info_edit_language).setText("")
        findViewById<EditText>(R.id.basic_info_edit_street).setText("")
        findViewById<EditText>(R.id.basic_info_edit_zipcode).setText("")

        // Set other UI elements as needed for creating a new entry
    }

    override fun setupUIForEdit(data: BasicInfo) {
        findViewById<EditText>(R.id.basic_info_edit_name).setText(data.name)
        // findViewById<EditText>(R.id.basic_info_edit_email).setText(data.email)

        findViewById<EditText>(R.id.basic_info_edit_phone).setText(data.phone)
        findViewById<EditText>(R.id.basic_info_edit_language).setText(data.language)
        findViewById<EditText>(R.id.basic_info_edit_street).setText(data.street)
        findViewById<EditText>(R.id.basic_info_edit_zipcode).setText(data.zipcode)


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
       // newData.email = findViewById<EditText>(R.id.basic_info_edit_email).text.toString()
        newData.imageUri = findViewById<ImageView>(R.id.editProfilePic).tag as Uri?
        newData.phone = findViewById<EditText>(R.id.basic_info_edit_phone).text.toString()
        newData.language = findViewById<EditText>(R.id.basic_info_edit_language).text.toString()
        newData.street = findViewById<EditText>(R.id.basic_info_edit_street).text.toString()
        newData.zipcode = findViewById<EditText>(R.id.basic_info_edit_zipcode).text.toString()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.setValue(newData)


        val resultIntent = Intent()
        resultIntent.putExtra(KEY_BASIC_INFO, newData)
        setResult(Activity.RESULT_OK, resultIntent)
        Log.d("BasicInfoEditActivity", "New data: $newData")
        finish()

    }

    override fun initializeData(): BasicInfo? {
        return intent.getParcelableExtra(KEY_BASIC_INFO)
    }

    private fun showImage(imageUri: Uri) {
        val imageView = findViewById<ImageView>(R.id.editProfilePic)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        imageView.tag = imageUri
        ImageUtils.loadImage(this, imageUri, imageView)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQ_CODE_PICK_IMAGE)
    }
}