package com.example.freelanceflow.activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.freelanceflow.R
import com.example.freelanceflow.model.Experience
import com.example.freelanceflow.util.DateUtils
import java.util.Arrays

class ExperienceEditActivity : EditBaseActivity<Experience>() {

    companion object {
        const val KEY_EXPERIENCE = "experience"
        const val KEY_EXPERIENCE_ID = "experience_id"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_experience_edit
    }

    override fun setupUIForCreate() {
        findViewById<View>(R.id.experience_edit_delete).visibility = View.VISIBLE
    }

    override fun setupUIForEdit(data: Experience) {
        findViewById<EditText>(R.id.experience_edit_company).setText(data.company)
        findViewById<EditText>(R.id.experience_edit_title).setText(data.title)
        findViewById<EditText>(R.id.experience_edit_start_date).setText(DateUtils.dateToString(data.startDate))
        findViewById<EditText>(R.id.experience_edit_end_date).setText(DateUtils.dateToString(data.endDate))
        findViewById<EditText>(R.id.experience_edit_details).setText(data.details?.let {
            TextUtils.join("\n",
                it
            )
        })

        findViewById<View>(R.id.experience_edit_delete).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_EXPERIENCE_ID, data.id)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun saveAndExit(data: Experience?) {
        var newData = data
        if (newData == null) {
            newData = Experience()
        }

        newData.company = findViewById<EditText>(R.id.experience_edit_company).text.toString()
        newData.title = findViewById<EditText>(R.id.experience_edit_title).text.toString()
        newData.startDate = DateUtils.stringToDate(findViewById<TextView>(R.id.experience_edit_start_date).text.toString())
        newData.endDate = DateUtils.stringToDate(findViewById<TextView>(R.id.experience_edit_end_date).text.toString())
        newData.details = Arrays.asList(*TextUtils.split(findViewById<EditText>(R.id.experience_edit_details).text.toString(), "\n"))

        val resultIntent = Intent()
        resultIntent.putExtra(KEY_EXPERIENCE, newData)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun initializeData(): Experience? {
        return intent.getParcelableExtra(KEY_EXPERIENCE)
    }
}
