package com.example.freelanceflow.activities

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.ic_save -> {
                saveAndExit(data)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun setupUIForCreate()

    protected abstract fun setupUIForEdit(@NonNull data: T)

    protected abstract fun saveAndExit(@Nullable data: T?)

    protected abstract fun initializeData(): T?
}
