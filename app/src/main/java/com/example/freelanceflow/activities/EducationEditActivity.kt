package com.example.freelanceflow.activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.freelanceflow.R
import com.example.freelanceflow.model.Education
import com.example.freelanceflow.util.DateUtils
import java.util.Arrays

class EducationEditActivity : EditBaseActivity<Education>() {

    companion object {
        const val KEY_EDUCATION = "education"
        const val KEY_EDUCATION_ID = "education_id"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_education_edit
    }

    override fun setupUIForCreate() {
        findViewById<View>(R.id.education_edit_delete).visibility = View.GONE
    }

    override fun setupUIForEdit(data: Education) {
        findViewById<EditText>(R.id.education_edit_school).setText(data.school)
        findViewById<EditText>(R.id.education_edit_major).setText(data.major)
        findViewById<EditText>(R.id.education_edit_courses).setText(data.courses?.let {
            TextUtils.join("\n",
                it
            )
        })
        findViewById<EditText>(R.id.education_edit_start_date).setText(DateUtils.dateToString(data.startDate))
        findViewById<EditText>(R.id.education_edit_end_date).setText(DateUtils.dateToString(data.endDate))

        findViewById<View>(R.id.education_edit_delete).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_EDUCATION_ID, data.id)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun saveAndExit(data: Education?) {
        var newData = data
        if (newData == null) {
            newData = Education()
        }

        newData.school = findViewById<EditText>(R.id.education_edit_school).text.toString()
        newData.major = findViewById<EditText>(R.id.education_edit_major).text.toString()
        newData.startDate = DateUtils.stringToDate(findViewById<TextView>(R.id.education_edit_start_date).text.toString())
        newData.endDate = DateUtils.stringToDate(findViewById<TextView>(R.id.education_edit_end_date).text.toString())
        newData.courses = Arrays.asList(*TextUtils.split(findViewById<EditText>(R.id.education_edit_courses).text.toString(), "\n"))

        val resultIntent = Intent()
        resultIntent.putExtra(KEY_EDUCATION, newData)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun initializeData(): Education? {
        return intent.getParcelableExtra(KEY_EDUCATION)
    }
}
