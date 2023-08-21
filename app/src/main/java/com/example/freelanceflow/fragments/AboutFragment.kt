package com.example.freelanceflow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.Pair
import com.example.freelanceflow.R
import com.example.freelanceflow.model.Experience
import com.example.freelanceflow.model.Project
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)

    }

    object DateUtils {
        fun dateToString(date: Date): String {
            val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = AboutFragmentArgs.fromBundle(requireArguments())
        val basicInfo = args.basicInfo
        //val educations = args.educations
        //val experiences = args.experiences
        //val projects = args.projects


        val profileImageView: CircleImageView = view.findViewById(R.id.profileImageView)
        profileImageView.setImageResource(R.drawable.defaultimage) // Set the image using Picasso or Glide

        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        nameTextView.text = args.basicInfo?.name

        val emailTextView: TextView = view.findViewById(R.id.emailTextView)
        emailTextView.text = args.basicInfo?.email

        //displayEducation(educations)
        //displayExperiences(experiences)
    //    displayProjects(projects)
    }
    private fun displayEducation(educations: List<Pair<Any, Any>>) {
        val educationStringBuilder = StringBuilder()

        for (education in educations) {
            val dateString = "${education.second?.let { DateUtils.dateToString(it as Date) }} ~ ${DateUtils.dateToString(Date())}"
            educationStringBuilder.append("${education.first} ($dateString)\n")
        }

        val educationTextView: TextView = requireView().findViewById(R.id.educationTextView)
        educationTextView.text = educationStringBuilder.toString()
    }

    private fun displayExperiences(experiences: List<Pair<Any, Any>>) {
        val experienceStringBuilder = StringBuilder()

        for (experience in experiences) {
            val dateString = "${experience.second?.let { DateUtils.dateToString(it as Date) }} ~ ${DateUtils.dateToString(Date())}"
            experienceStringBuilder.append("${experience.first} ($dateString)\n")
        }

        val experienceTextView: TextView = requireView().findViewById(R.id.experienceTextView)
        experienceTextView.text = experienceStringBuilder.toString()
    }

    private fun displayProjects(projects: List<Pair<Any, Any>>) {
        val projectStringBuilder = StringBuilder()

        for (projectPair in projects) {
            val projectName = projectPair.first.toString()
            val dateRange = "${projectPair.second?.let { DateUtils.dateToString(it as Date) }} ~ ${DateUtils.dateToString(Date())}"
            projectStringBuilder.append("$projectName ($dateRange)\n")
        }

        val projectsTextView: TextView = requireView().findViewById(R.id.projectsTextView)
        projectsTextView.text = projectStringBuilder.toString()
    }

}
