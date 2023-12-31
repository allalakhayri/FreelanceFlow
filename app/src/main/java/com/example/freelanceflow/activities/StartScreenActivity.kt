package com.example.freelanceflow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.freelanceflow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartScreenActivity : AppCompatActivity() {
    private  lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)
        supportActionBar?.hide()
        auth= Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({

             // if there's an account already go to home
                val intent= Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()

        },3000 )



    }
}