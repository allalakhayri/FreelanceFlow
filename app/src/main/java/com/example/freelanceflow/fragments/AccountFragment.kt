    package com.example.freelanceflow.fragments

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.TextView
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.commit
    import androidx.navigation.findNavController
    import at.markushi.ui.CircleButton
    import com.example.freelanceflow.R
    import com.example.freelanceflow.databinding.FragmentAccountBinding
    import com.example.freelanceflow.databinding.FragmentEditProfileBinding
    import com.example.freelanceflow.model.BasicInfo
    import com.example.freelanceflow.model.ClientProfile
    import com.example.freelanceflow.model.Education
    import com.example.freelanceflow.model.Experience
    import com.example.freelanceflow.model.Project
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener
    import com.google.firebase.firestore.FirebaseFirestore
    import com.squareup.picasso.Picasso
    import de.hdodenhof.circleimageview.CircleImageView

    class AccountFragment : Fragment() {
        private lateinit var auth: FirebaseAuth

        private lateinit var binding: FragmentAccountBinding
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentAccountBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Initialize FirebaseAuth instance
            auth = FirebaseAuth.getInstance()
            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
            currentUserUid?.let { uid ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userProfile = snapshot.getValue(ClientProfile::class.java)
                            val profileImageUri = userProfile?.profileImageUri
                            val userName = userProfile?.name

                            // Use Picasso to load and display the image
                            Picasso.get()
                                .load(profileImageUri)
                                .placeholder(R.drawable.defaultimage) // Placeholder image while loading
                                .error(R.drawable.defaultimage) // Error image if loading fails
                                .into(binding.profileImage)
                            binding.username.text = "Hello, $userName"

                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Handle the error
                    }
                })
            }


            // Set click listeners for buttons
          //  aboutButton.setOnClickListener {
            //   val direction=AccountFragmentDirections.actionAccountFragmentToAboutFragment()
              //  it.findNavController().navigate(direction)
            //}

            binding.editButton.setOnClickListener {
                val direction=AccountFragmentDirections.actionAccountFragmentToEditProfileFragment()
                it.findNavController().navigate(direction)
            }

           binding.dashboardButton.setOnClickListener {
                val direction=AccountFragmentDirections.actionAccountFragmentToDashboardFragment()
                it.findNavController().navigate(direction)
            }
            binding.logoutButton.setOnClickListener {
                auth.signOut()
                val direction = AccountFragmentDirections.actionAccountFragmentToOnBoardingActivity()
                val navController = it.findNavController()
                navController.navigate(direction)
                // Pop all back stack entries except the start destination
                while (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }

            }

        }
    }
