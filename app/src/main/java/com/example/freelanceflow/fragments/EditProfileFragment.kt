package com.example.freelanceflow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.FragmentEditProfileBinding
import com.example.freelanceflow.model.ClientProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the user's profile information from the database
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(ClientProfile::class.java)

                        // Populate the EditText fields with the retrieved data
                        binding.tvClientName.text = userProfile?.name
                        binding.editProfilePhone.setText(userProfile?.mobileNumber)
                        binding.editProfileStreet.setText(userProfile?.streetAddress)
                        binding.editProfileCity.setText(userProfile?.city)
                        // Set the selected values for the Spinners
                        val genderAdapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.genders_array,
                            android.R.layout.simple_spinner_item
                        )
                        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerGender.adapter = genderAdapter
                        val genderPosition = genderAdapter.getPosition(userProfile?.gender)
                        binding.spinnerGender.setSelection(genderPosition)
                        //
                        val languageAdapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.languages_array,
                            android.R.layout.simple_spinner_item
                        )
                        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerLanguage.adapter = languageAdapter
                        val languagePosition = languageAdapter.getPosition(userProfile?.language)
                        binding.spinnerLanguage.setSelection(languagePosition)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }


        // Set up other UI components and handle user interactions here
        binding.btnSaveProfile.setOnClickListener {
            val newProfile = ClientProfile(
                profileImageUri = null, // Set the new profile image URI if applicable
                name = binding.tvClientName.text.toString(),
                mobileNumber = binding.editProfilePhone.text.toString(),
                streetAddress = binding.editProfileStreet.text.toString(),
                city = binding.editProfileCity.text.toString(),
                language = binding.spinnerLanguage.selectedItem.toString(),
                gender = binding.spinnerGender.selectedItem.toString()
            )

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
            currentUserUid?.let { uid ->
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("users").child(uid)
                databaseReference.setValue(newProfile)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Profile updated successfully !",
                            Toast.LENGTH_SHORT
                        ).show()
                        parentFragmentManager.popBackStack()


                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Please fill all the fields!", Toast.LENGTH_SHORT)
                            .show()

                    }
            }
        }
    }
    }


