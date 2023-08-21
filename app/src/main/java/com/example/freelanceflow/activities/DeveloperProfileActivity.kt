package com.example.freelanceflow.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.ActivityDeveloperProfileBinding
import com.example.freelanceflow.fragments.AccountFragment
//import com.example.freelanceflow.databinding.ActivityDeveloperProfileBinding
//import com.example.freelanceflow.databinding.ActivityHomeBinding
import com.google.gson.reflect.TypeToken
import com.example.freelanceflow.model.BasicInfo
import com.example.freelanceflow.model.Education
import com.example.freelanceflow.model.Experience
import com.example.freelanceflow.model.Project
import com.example.freelanceflow.util.DateUtils
import com.example.freelanceflow.util.ImageUtils
import com.example.freelanceflow.util.ModelUtils
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.GrooveBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class DeveloperProfileActivity : AppCompatActivity() {

    companion object {
        private const val REQ_CODE_EDIT_EDUCATION = 100
        private const val REQ_CODE_EDIT_EXPERIENCE = 101
        private const val REQ_CODE_EDIT_PROJECT = 102
        private const val REQ_CODE_EDIT_BASIC_INFO = 103

        private const val MODEL_EDUCATIONS = "educations"
        private const val MODEL_EXPERIENCES = "experiences"
        private const val MODEL_PROJECTS = "projects"
        private const val MODEL_BASIC_INFO = "basic_info"
    }

    private lateinit var basicInfo: BasicInfo
    private lateinit var educations: MutableList<Education>
    private lateinit var experiences: MutableList<Experience>
    private lateinit var projects: MutableList<Project>

    private lateinit var resumeBinding: ActivityDeveloperProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        loadData()
        setupUI()
        openAccountFragment()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_EDIT_BASIC_INFO -> {
                    val basicInfo = data?.getParcelableExtra<BasicInfo>(BasicInfoEditActivity.KEY_BASIC_INFO)
                    updateBasicInfo(basicInfo)
                }
                REQ_CODE_EDIT_EDUCATION -> {
                    val educationId = data?.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID)
                    if (educationId != null) {
                        deleteEducation(educationId)
                    } else {
                        val education = data?.getParcelableExtra<Education>(EducationEditActivity.KEY_EDUCATION)
                        education?.let { updateEducation(it) }
                    }
                }
                REQ_CODE_EDIT_EXPERIENCE -> {
                    val experienceId = data?.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID)
                    if (experienceId != null) {
                        deleteExperience(experienceId)
                    } else {
                        val experience = data?.getParcelableExtra<Experience>(ExperienceEditActivity.KEY_EXPERIENCE)
                        experience?.let { updateExperience(it) }
                    }
                }
                REQ_CODE_EDIT_PROJECT -> {
                    val projectId = data?.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID)
                    if (projectId != null) {
                        deleteProject(projectId)
                    } else {
                        val project = data?.getParcelableExtra<Project>(ProjectEditActivity.KEY_PROJECT)
                        project?.let { updateProject(it) }
                    }
                }
            }
        }
    }

    private fun setupUI() {
       setContentView(R.layout.activity_developer_profile)
        findViewById<Button>(R.id.btnResume).setOnClickListener{
            checkStoragePermission()

        }
        findViewById<Button>(R.id.btnNext).setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        findViewById<ImageButton>(R.id.add_education_btn).setOnClickListener {
            val intent = Intent(this@DeveloperProfileActivity, EducationEditActivity::class.java)
            startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION)
        }

        findViewById<ImageButton>(R.id.add_experience_btn).setOnClickListener {
            val intent = Intent(this@DeveloperProfileActivity, ExperienceEditActivity::class.java)
            startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE)
        }

        findViewById<ImageButton>(R.id.add_project_btn).setOnClickListener {
            val intent = Intent(this@DeveloperProfileActivity ,ProjectEditActivity::class.java)
            startActivityForResult(intent, REQ_CODE_EDIT_PROJECT)
        }

        setupBasicInfo()
        setupEducations()
        setupExperiences()
        setupProjects()
    }

    private fun setupBasicInfo() {
        findViewById<TextView>(R.id.name).text = if (basicInfo.name.isNullOrEmpty()) "Your name" else basicInfo.name
        findViewById<TextView>(R.id.email).text = if (basicInfo.email.isNullOrEmpty()) "Your email" else basicInfo.email

        val userPicture = findViewById<ImageView>(R.id.user_picture)
        basicInfo.imageUri?.let { ImageUtils.loadImage(this, it, userPicture) }
            ?: userPicture.setImageResource(R.drawable.user_ghost)

        findViewById<View>(R.id.edit_basic_info).setOnClickListener {
            val intent = Intent(this@DeveloperProfileActivity, BasicInfoEditActivity::class.java)
            intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo)
            startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO)
        }
    }

    private fun setupEducations() {
        val educationsLayout = findViewById<LinearLayout>(R.id.education_list)
        educationsLayout.removeAllViews()
        for (education in educations) {
            val educationView = layoutInflater.inflate(R.layout.education_item, null)
            setupEducation(educationView, education)
            educationsLayout.addView(educationView)
        }
    }

    private fun setupEducation(educationView: View, education: Education) {
        val dateString = "${DateUtils.dateToString(education.startDate)} ~ ${DateUtils.dateToString(education.endDate)}"
        educationView.findViewById<TextView>(R.id.education_school)
            .text = "${education.school} ${education.major} ($dateString)"
        educationView.findViewById<TextView>(R.id.education_courses)
            .text = education.courses?.let { formatItems(it) }

        educationView.findViewById<ImageButton>(R.id.edit_education_btn).setOnClickListener {
            val intent = Intent(this@DeveloperProfileActivity, EducationEditActivity::class.java)
            intent.putExtra(EducationEditActivity.KEY_EDUCATION, education)
            startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION)
        }
    }

    private fun setupExperiences() {
        val experiencesLayout = findViewById<LinearLayout>(R.id.experience_list)
        experiencesLayout.removeAllViews()
        for (experience in experiences) {
            val experienceView = layoutInflater.inflate(R.layout.experience_item, null)
            setupExperience(experienceView, experience)
            experiencesLayout.addView(experienceView)
        }
    }

    private fun setupExperience(experienceView: View, experience: Experience) {
        val dateString = "${DateUtils.dateToString(experience.startDate)} ~ ${DateUtils.dateToString(experience.endDate)}"
        experienceView.findViewById<TextView>(R.id.experience_company)
            .text = "${experience.company} ($dateString)"
        experienceView.findViewById<TextView>(R.id.experience_details)
            .text = experience.details?.let { formatItems(it) }

        experienceView.findViewById<ImageButton>(R.id.edit_experience_btn).setOnClickListener {
            val intent = Intent(this@DeveloperProfileActivity, ExperienceEditActivity::class.java)
            intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience)
            startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE)
        }
    }

    private fun setupProjects() {
        val projectListLayout = findViewById<LinearLayout>(R.id.project_list)
        projectListLayout.removeAllViews()
        for (project in projects) {
            val projectView = layoutInflater.inflate(R.layout.project_item, null)
            setupProject(projectView, project)
            projectListLayout.addView(projectView)
        }
    }

    private fun setupProject(projectView: View, project: Project) {
        val dateString = "${DateUtils.dateToString(project.startDate)} ~ ${DateUtils.dateToString(project.endDate)}"
        projectView.findViewById<TextView>(R.id.project_name)
            .text = "${project.name} ($dateString)"
        projectView.findViewById<TextView>(R.id.project_details)
            .text = project.details?.let { formatItems(it) }

        projectView.findViewById<ImageButton>(R.id.edit_project_btn).setOnClickListener {
            val intent = Intent(this@DeveloperProfileActivity, ProjectEditActivity::class.java)
            intent.putExtra(ProjectEditActivity.KEY_PROJECT, project)
            startActivityForResult(intent, REQ_CODE_EDIT_PROJECT)
        }
    }

    private fun loadData() {
        val savedBasicInfo = ModelUtils.read(this@DeveloperProfileActivity, MODEL_BASIC_INFO, object : TypeToken<BasicInfo>() {})
        basicInfo = savedBasicInfo ?: BasicInfo()

        val savedEducation = ModelUtils.read(this@DeveloperProfileActivity, MODEL_EDUCATIONS, object : TypeToken<List<Education>>() {})
        educations = savedEducation?.toMutableList() ?: mutableListOf()

        val savedExperience = ModelUtils.read(this@DeveloperProfileActivity, MODEL_EXPERIENCES, object : TypeToken<List<Experience>>() {})
        experiences = savedExperience?.toMutableList() ?: mutableListOf()

        val savedProjects = ModelUtils.read(this@DeveloperProfileActivity, MODEL_PROJECTS, object : TypeToken<List<Project>>() {})
        projects = savedProjects?.toMutableList() ?: mutableListOf()
    }




    private fun updateBasicInfo(basicInfo: BasicInfo?) {
        basicInfo?.let {
            ModelUtils.save(this, MODEL_BASIC_INFO, it)
            this.basicInfo = it
            setupBasicInfo()
        }
    }

    private fun updateEducation(education: Education) {
        val foundIndex = educations.indexOfFirst { it.id == education.id }
        if (foundIndex != -1) {
            educations[foundIndex] = education
        } else {
            educations.add(education)
        }
        ModelUtils.save(this, MODEL_EDUCATIONS, educations)
        setupEducations()
    }

    private fun updateExperience(experience: Experience) {
        val foundIndex = experiences.indexOfFirst { it.id == experience.id }
        if (foundIndex != -1) {
            experiences[foundIndex] = experience
        } else {
            experiences.add(experience)
        }
        ModelUtils.save(this, MODEL_EXPERIENCES, experiences)
        setupExperiences()
    }

    private fun updateProject(project: Project) {
        val foundIndex = projects.indexOfFirst { it.id == project.id }
        if (foundIndex != -1) {
            projects[foundIndex] = project
        } else {
            projects.add(project)
        }
        ModelUtils.save(this, MODEL_PROJECTS, projects)
        setupProjects()
    }

    private fun deleteEducation(educationId: String) {
        educations.removeIf { it.id == educationId }
        ModelUtils.save(this, MODEL_EDUCATIONS, educations)
        setupEducations()
    }

    private fun deleteExperience(experienceId: String) {
        experiences.removeIf { it.id == experienceId }
        ModelUtils.save(this, MODEL_EXPERIENCES, experiences)
        setupExperiences()
    }

    private fun deleteProject(projectId: String) {
        projects.removeIf { it.id == projectId }
        ModelUtils.save(this, MODEL_PROJECTS, projects)
        setupProjects()
    }

    private fun formatItems(items: List<String>): String {
        val sb = StringBuilder()
        for (item in items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n')
        }
        if (sb.isNotEmpty()) {
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }


    private fun checkStoragePermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.let {
                        if (it.areAllPermissionsGranted()) {
                            createResume()
                        } else {
                            Toast.makeText(
                                this@DeveloperProfileActivity,
                                "Please Grant All Permissions",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }

    private fun openAccountFragment() {
        val accountFragment = AccountFragment.newInstance(
            basicInfo,
            educations,
            experiences,
            projects
        )

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, accountFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun createResume() {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val currentTime = System.currentTimeMillis().toString()
        val child = "$currentTime.pdf"
        val file = File(pdfPath,child)
        val outputStream = FileOutputStream(file)

        // iText- Pdf
        val pdfWriter = PdfWriter(file)
        val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        //creating PDF
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(10f, 10f, 10f, 10f)

        // Extract name and email from the layout
        val nameText = resumeBinding.name.text.toString()
        val emailText = resumeBinding.email.text.toString()

        // Create paragraphs for name and email
        val name = Paragraph(nameText).setBold().setFontSize(30f).setTextAlignment(TextAlignment.CENTER)
        val email = Paragraph(emailText).setBold().setFontSize(24f).setTextAlignment(TextAlignment.CENTER)

        // Add name and email to the document
        document.add(name)
        document.add(email)

        // Extract the image from the ImageView
        val userPictureView = findViewById<ImageView>(R.id.user_picture)
        val userPictureBitmap = (userPictureView.drawable as BitmapDrawable).bitmap

        // Convert the bitmap to a byte array
        val stream = ByteArrayOutputStream()
        userPictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val userPictureData = stream.toByteArray()

        // Create an ImageData instance from the byte array
        val imageData = ImageDataFactory.create(userPictureData)
        val userPicture = Image(imageData)

        // Add the user picture to the PDF document
        document.add(userPicture)


        // Extract and add education details
        val educationLayout = findViewById<LinearLayout>(R.id.education_list)
        for (i in 0 until educationLayout.childCount) {
            val educationView = educationLayout.getChildAt(i)
            val educationText = educationView.findViewById<TextView>(R.id.education_school).text.toString()
            val educationParagraph = Paragraph(educationText).setFontSize(20f)
            document.add(educationParagraph)
        }

        // Extract and add experience details
        val experienceLayout = findViewById<LinearLayout>(R.id.experience_list)
        for (i in 0 until experienceLayout.childCount) {
            val experienceView = experienceLayout.getChildAt(i)
            val experienceText = experienceView.findViewById<TextView>(R.id.experience_company).text.toString()
            val experienceParagraph = Paragraph(experienceText).setFontSize(20f)
            document.add(experienceParagraph)
        }

        // Extract and add project details
        val projectLayout = findViewById<LinearLayout>(R.id.project_list)
        for (i in 0 until projectLayout.childCount) {
            val projectView = projectLayout.getChildAt(i)
            val projectText = projectView.findViewById<TextView>(R.id.project_name).text.toString()
            val projectParagraph = Paragraph(projectText).setFontSize(20f)
            document.add(projectParagraph)
        }

        // Close the document
        document.close()
        Toast.makeText(this@DeveloperProfileActivity, "PDF Downloaded", Toast.LENGTH_SHORT).show()
    }







}
