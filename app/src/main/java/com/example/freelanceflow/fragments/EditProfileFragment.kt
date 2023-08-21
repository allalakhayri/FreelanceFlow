package com.example.freelanceflow.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.freelanceflow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileFragment : Fragment() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var storageReference: StorageReference


    private lateinit var profileImage: CircleImageView
    private lateinit var profileName: EditText
    private lateinit var profilePhone: EditText
    private lateinit var profileStreet: EditText
    private lateinit var profileZipCode: EditText
    private lateinit var profileLanguage: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageReference = FirebaseStorage.getInstance().reference.child("profile_images")

        profileImage = view.findViewById(R.id.editProfileImage)
        profileName = view.findViewById(R.id.editProfileName)
        profilePhone = view.findViewById(R.id.editProfilePhone)
        profileStreet = view.findViewById(R.id.editProfileStreet)
        profileZipCode = view.findViewById(R.id.editProfileZipCode)
        profileLanguage = view.findViewById(R.id.editProfileLanguage)
        radioGroupGender = view.findViewById(R.id.radioGroupGender)
        saveButton = view.findViewById(R.id.saveButton)

        // Set click listener for changing profile photo
        profileImage.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        }


        saveButton.setOnClickListener {
            // Save the edited profile information
            // You can implement your logic here

            // After saving, navigate back to the previous fragment
            parentFragmentManager.popBackStack()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data

            val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

            val uploadTask = selectedImage?.let { imageRef.putFile(it) }
            uploadTask?.addOnSuccessListener {


                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()

                    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                    currentUserUid?.let { uid ->
                        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                        databaseReference.child("profileImageUrl").setValue(imageUrl)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()

                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()

                            }
                    }
                }
            }?.addOnFailureListener {
                Toast.makeText(context, "No image uploaded!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
