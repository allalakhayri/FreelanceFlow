package com.example.freelanceflow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import at.markushi.ui.CircleButton
import com.example.freelanceflow.R
import com.example.freelanceflow.model.BasicInfo
import com.example.freelanceflow.model.Education
import com.example.freelanceflow.model.Experience
import com.example.freelanceflow.model.Project
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AccountFragment : Fragment() {
    companion object {
        private const val ARG_BASIC_INFO = "basic_info"
        private const val ARG_EDUCATIONS = "educations"
        private const val ARG_EXPERIENCES = "experiences"
        private const val ARG_PROJECTS = "projects"

        fun newInstance(
            basicInfo: BasicInfo,
            educations: List<Education>,
            experiences: List<Experience>,
            projects: List<Project>
        ): AccountFragment {
            val fragment = AccountFragment()
            val args = Bundle()
            args.putParcelable(ARG_BASIC_INFO, basicInfo)
            args.putParcelableArrayList(ARG_EDUCATIONS, ArrayList(educations))
            args.putParcelableArrayList(ARG_EXPERIENCES, ArrayList(experiences))
            args.putParcelableArrayList(ARG_PROJECTS, ArrayList(projects))
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val basicInfo = arguments?.getParcelable<BasicInfo>(ARG_BASIC_INFO)
        val educations = arguments?.getParcelableArrayList<Education>(ARG_EDUCATIONS)
        val experiences = arguments?.getParcelableArrayList<Experience>(ARG_EXPERIENCES)
        val projects = arguments?.getParcelableArrayList<Project>(ARG_PROJECTS)

        val basicInfoTextView: TextView = view.findViewById(R.id.username)
        basicInfoTextView.text = basicInfo?.name ?: "Default Name"

        val profileImageView: CircleImageView = view.findViewById(R.id.Iconimage2)
        basicInfo?.imageUri?.let { imageUrl ->
            // Load the image using Picasso
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.defaultimage) // Placeholder image while loading
                .error(R.drawable.defaultimage) // Image to display on error
                .into(profileImageView)
        } ?: run {
            // If imageUri is null, set a default image
            profileImageView.setImageResource(R.drawable.defaultimage)
        }


        val iconImage: CircleImageView = view.findViewById(R.id.Iconimage2)
        val aboutButton: CircleButton = view.findViewById(R.id.AboutButton)
        val editButton: CircleButton = view.findViewById(R.id.EditButton)
        val dashboardButton: CircleButton = view.findViewById(R.id.DashboardButton)

        // Set click listeners for buttons
        aboutButton.setOnClickListener {
           val direction=AccountFragmentDirections.actionAccountFragmentToAboutFragment(
               basicInfo!!,
               educations,
               experiences,
               projects
           )
            it.findNavController().navigate(direction)
        }

        editButton.setOnClickListener {
            val direction=AccountFragmentDirections.actionAccountFragmentToEditProfileFragment()
            it.findNavController().navigate(direction)
        }

       dashboardButton.setOnClickListener {
            val direction=AccountFragmentDirections.actionAccountFragmentToDashboardFragment()
            it.findNavController().navigate(direction)
        }


    }
}
