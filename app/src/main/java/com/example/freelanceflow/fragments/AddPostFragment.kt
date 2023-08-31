package com.example.freelanceflow.fragments

import android.icu.text.CaseMap.Title
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.FragmentAddPostBinding
import com.example.freelanceflow.viewmodels.AddPostViewModel
import com.example.freelanceflow.viewmodels.AddPostViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPostFragment : Fragment(R.layout.fragment_add_post) {
    private lateinit var addPostBinding: FragmentAddPostBinding
    private lateinit var viewModel: AddPostViewModel
    private lateinit var imageUri: Uri
    private var isAddingPost = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPostBinding = FragmentAddPostBinding.bind(view)
        viewModel = ViewModelProvider(this, AddPostViewModelFactory(requireActivity().contentResolver))
            .get(AddPostViewModel::class.java)

        val contracts = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                imageUri = it
            }
            addPostBinding.addPostImage.setImageURI(imageUri)
        }

        addPostBinding.addPostImage.setOnClickListener {
            contracts.launch("image/*")
        }

        addPostBinding.btnPost.setOnClickListener {
            if (!isAddingPost) {

                isAddingPost = true
                addPostBinding.btnPost.isEnabled = false
                addPostBinding.loadingContainer.visibility = View.VISIBLE

                val inputPost = addPostBinding.tvPostDescription.text.toString()
                val readTimeString=addPostBinding.postReadTime.toString()
                val title=addPostBinding.postTitle.text.toString()
                val readTime = readTimeString.toIntOrNull() ?: 0
                if (inputPost.isNotEmpty() && imageUri != null) {
                    viewModel.addPost(imageUri,title,readTime,inputPost)
                } else {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    addPostBinding.loadingContainer.visibility = View.GONE
                    addPostBinding.btnPost.isEnabled = true
                }
            }
        }

        viewModel.postAdded.observe(viewLifecycleOwner) { isSuccess ->
            addPostBinding.loadingContainer.visibility = View.GONE
            if (isSuccess) {
                Toast.makeText(context, "Blog Added Successfully", Toast.LENGTH_SHORT).show()
                val direction = AddPostFragmentDirections.actionAddPostFragmentToPostsFragment()
                findNavController().navigate(direction)
            } else {
                Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show()
                addPostBinding.btnPost.isEnabled = true
            }
        }
    }
}
