package com.example.freelanceflow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.ActivityHomeBinding
import com.example.freelanceflow.databinding.ActivitySignInBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.clientCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.developerCheckbox.isChecked = false
            }
        }

        binding.developerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.clientCheckbox.isChecked = false
            }
        }
        binding.createAccountButton.setOnClickListener {
            if (binding.clientCheckbox.isChecked) {
                val intent = Intent(this@HomeActivity, SignUpActivity::class.java)
                intent.putExtra("isClientChecked", binding.clientCheckbox.isChecked)
                startActivity(intent)
            } else if (binding.developerCheckbox.isChecked) {
                val intent = Intent(this@HomeActivity, SignUpActivity::class.java)
               intent.putExtra("isDeveloperChecked", binding.developerCheckbox.isChecked)
                startActivity(intent)
            }
        }

        binding.loginLink.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}