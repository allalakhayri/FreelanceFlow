package com.example.freelanceflow.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.ActivityClientProfileBinding
import com.example.freelanceflow.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase

class ClientProfileActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityClientProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_profile)


        // Spinner data
        val languages = listOf("English", "Spanish", "French", "German", "Other")
        val genders = listOf("Male", "Female", "Other")
        val countries = listOf("United States", "Canada", "United Kingdom", "Germany", "Other")

        // Create adapters for spinners
        val languageAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)

        // Set adapters for spinners
       // binding.languageSpinner.adapter = languageAdapter
       // binding.genderSpinner.adapter = genderAdapter
        //binding.countrySpinner.adapter = countryAdapter

        // Save Profile button click listener
        //binding.saveProfileBtn.setOnClickListener {
          //  Toast.makeText(this,"Success !", Toast.LENGTH_SHORT).show()


       // }
}


}
