package com.example.freelanceflow.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment(R.layout.fragment_account) {
    lateinit var accountBinding: FragmentAccountBinding
    lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountBinding = FragmentAccountBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        accountBinding.tvUserEmail.text = auth.currentUser?.email.toString()

        accountBinding.btnLogout.setOnClickListener {
            auth.signOut()
           // val direction = AccountFragmentDirections.actionAccountFragmentToOnBoardingActivity()
            //it.findNavController().navigate(direction)
            findNavController().navigate(R.id.action_accountFragment_to_onBoardingActivity)
        }
    }

}