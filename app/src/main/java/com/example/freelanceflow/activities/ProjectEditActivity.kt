package com.example.freelanceflow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.freelanceflow.R
import com.example.freelanceflow.model.Project
import com.example.freelanceflow.util.DateUtils
import java.util.Arrays

class ProjectEditActivity : EditBaseActivity<Project>() {

    companion object {
        const val KEY_PROJECT = "project"
        const val KEY_PROJECT_ID = "project_id"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_project_edit
    }

    override fun setupUIForCreate() {
        findViewById<View>(R.id.project_edit_delete).visibility = View.GONE
    }

    override fun setupUIForEdit(data: Project) {
        findViewById<EditText>(R.id.project_edit_name).setText(data.name)
        findViewById<EditText>(R.id.project_edit_start_date).setText(DateUtils.dateToString(data.startDate))
        findViewById<EditText>(R.id.project_edit_end_date).setText(DateUtils.dateToString(data.endDate))
        findViewById<EditText>(R.id.project_edit_details).setText(data.details?.let {
            TextUtils.join("\n",
                it
            )
        })

        findViewById<View>(R.id.project_edit_delete).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_PROJECT_ID, data.id)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun saveAndExit(data: Project?) {
        var newData = data
        if (newData == null) {
            newData = Project()
        }

        newData.name = findViewById<EditText>(R.id.project_edit_name).text.toString()
        newData.startDate = DateUtils.stringToDate(findViewById<EditText>(R.id.project_edit_start_date).text.toString())
        newData.endDate = DateUtils.stringToDate(findViewById<EditText>(R.id.project_edit_end_date).text.toString())
        newData.details = Arrays.asList(*TextUtils.split(findViewById<EditText>(R.id.project_edit_details).text.toString(), "\n"))

        val resultIntent = Intent()
        resultIntent.putExtra(KEY_PROJECT, newData)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun initializeData(): Project? {
        return intent.getParcelableExtra(KEY_PROJECT)
    }
}
