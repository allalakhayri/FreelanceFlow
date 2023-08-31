import android.Manifest
import android.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freelanceflow.databinding.ActivityClientProfileBinding
import com.example.freelanceflow.model.ClientProfile


class ClientProfileViewModel : ViewModel() {


    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 101
        private const val PERMISSION_CODE = 100
    }

    private val _userProfileLiveData = MutableLiveData<ClientProfile>()
    val userProfileLiveData: LiveData<ClientProfile> = _userProfileLiveData

    private var selectedImageUri: Uri? = null


    fun handleImagePickerResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let { _userProfileLiveData.value = _userProfileLiveData.value?.copy(profileImageUri = it) }
            //binding.ivProfileImage.setImageURI(selectedImageUri)
        }
    }
    fun setupUI(activity: Activity, binding: ActivityClientProfileBinding) {
        binding.btnSelectImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openImagePicker(activity)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
            }
        }

        val languages = arrayOf("English", "French", "Arabic")
        val gender = arrayOf("Male", "Female")

        with(binding) {
            spinnerLanguage.adapter = ArrayAdapter(activity, R.layout.simple_spinner_item, languages)
            spinnerGender.adapter = ArrayAdapter(activity, R.layout.simple_spinner_item, gender)
            //ivProfileImage.setImageURI(_userProfileLiveData.value?.profileImageUri)

        }
    }


    fun openImagePicker(activity: Activity) {
        // You can use this method to trigger the image picker
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }
    fun getProfileImageUri(): Uri? {
        return _userProfileLiveData.value?.profileImageUri
    }

    // Other methods to update the user profile
    fun updateUserProfile(userProfile: ClientProfile) {
        _userProfileLiveData.value = userProfile
    }

}