package com.example.freelanceflow.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Button
import com.example.freelanceflow.R

abstract class EditBaseActivity<T> : AppCompatActivity() {

    private var data: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        data = initializeData()
        data?.let {
            setupUIForEdit(it)
        } ?: run {
            setupUIForCreate()
        }

        val saveButton: Button = findViewById(R.id.saveBasicButton)
        saveButton.setOnClickListener {
            saveAndExit(data)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun setupUIForCreate()

    protected abstract fun setupUIForEdit(data: T)

    protected abstract fun saveAndExit(data: T?)

    protected abstract fun initializeData(): T?
}
