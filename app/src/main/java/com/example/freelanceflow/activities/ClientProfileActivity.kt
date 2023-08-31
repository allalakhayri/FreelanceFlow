package com.example.freelanceflow.activities

import ClientProfileViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.freelanceflow.databinding.ActivityClientProfileBinding
import com.example.freelanceflow.model.ClientProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ClientProfileActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_CODE = 100
    }

    private lateinit var binding: ActivityClientProfileBinding
    private lateinit var viewModel: ClientProfileViewModel
    // private var viewModel: ClientProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ClientProfileViewModel::class.java)
        viewModel.userProfileLiveData.observe(this) { userProfile ->
            binding.ivProfileImage.setImageURI(userProfile.profileImageUri)
        }
        viewModel.setupUI(this, binding)
        binding.btnSaveProfile.setOnClickListener {
            val profileImageUri = viewModel.getProfileImageUri()
            val name = binding.tvclientName.text.toString()
            val mobileNumber = binding.tvclientNumber.text.toString()
            val streetAddress = binding.tvclientStreet.text.toString()
            val city = binding.tvclientCity.text.toString()
            val language = binding.spinnerLanguage.selectedItem.toString()
            val gender = binding.spinnerGender.selectedItem.toString()

            if (name.isEmpty() || mobileNumber.isEmpty() || streetAddress.isEmpty() || city.isEmpty()) {
                // Display a toast or a message indicating that all fields are required
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
            currentUserUid?.let { uid ->
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("users").child(uid)
                val clientProfile = ClientProfile(
                    profileImageUri = profileImageUri,
                    name = name,
                    mobileNumber = mobileNumber,
                    streetAddress = streetAddress,
                    city = city,
                    language = language,
                    gender = gender
                )
                // Save the client profile to the database
                databaseReference.setValue(clientProfile)

                SessionManager.getInstance(this).saveAuthToken("your_generated_auth_token_here")

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }
        private val requestImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                viewModel.handleImagePickerResult(result.resultCode, result.data)
                Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
            }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.openImagePicker(this)
            } else {
                Toast.makeText(this, "Permission denied. Cannot select image.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

}

