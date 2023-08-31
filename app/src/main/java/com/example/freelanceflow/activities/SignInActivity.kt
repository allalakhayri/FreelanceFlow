package com.example.freelanceflow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.ActivitySignUpBinding
import com.example.freelanceflow.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth=Firebase.auth
        val sessionManager = SessionManager.getInstance(this)
        binding.btnSignIn.setOnClickListener{
            val email=binding.etEmail.text.toString()
            val password=binding.etPassword.text.toString()
            if(checkAllField()){
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val user = auth.currentUser
                        user?.let {
                            sessionManager.saveAuthToken(it.uid)


                            Toast.makeText(this, "Sign in successfully!", Toast.LENGTH_SHORT).show()
                            //go to another activity (Home)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else {
                        Log.e("Error", it.exception.toString())
                    }
                }

            }

        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private  fun checkAllField():Boolean{
        val email=binding.etEmail.text.toString()
        if(email==""){
            binding.textInputLayoutEmail.error="This is a required field! "
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.textInputLayoutEmail.error="Check email format!"
            return false
        }

        if(binding.etPassword.text.toString()==""){
            binding.textInputLayoutPassword.error="This is a required field! "
            binding.textInputLayoutPassword.errorIconDrawable=null
            return false
        }
        if(binding.etPassword.length()<8){
            binding.textInputLayoutPassword.error="Password should at least 8 caracters long "
            binding.textInputLayoutPassword.errorIconDrawable=null
            return false
        }
        return true
    }
}