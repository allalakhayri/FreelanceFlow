package com.example.freelanceflow.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freelanceflow.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity:AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=Firebase.auth
        binding.btnSignUp.setOnClickListener{
            val email=binding.etEmail.text.toString()
            val password=binding.etPassword.text.toString()
            if(checkAllField()){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Account created successfully!",Toast.LENGTH_SHORT).show()
                        //go to another activity (Home)
                        val isClientChecked = intent.getBooleanExtra("isClientChecked", false)
                        val isDeveloperChecked = intent.getBooleanExtra("isDeveloperChecked", false)

                        if(isClientChecked){
                            val intent=Intent(this, ClientProfileActivity::class.java)
                            startActivity(intent)
                           // finish()
                        }
                        else if(isDeveloperChecked){
                            val intent=Intent(this, DeveloperProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }
                    else {
                        Log.e("Error" , it.exception.toString())
                    }
                }
            }

        }


        binding.tvSignIn.setOnClickListener {
           val intent = Intent(this, SignInActivity::class.java)
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
        }

        if(binding.etPassword.text.toString()==""){
            binding.textInputLayoutPassword.error="This is a required field! "
            binding.textInputLayoutPassword.errorIconDrawable=null
            return false
        }
        if(binding.etPassword.length()<8){
            binding.textInputLayoutPassword.error="Password should be at least 8 caracters long "
            binding.textInputLayoutPassword.errorIconDrawable=null
            return false
        }

        if(binding.etConfirmPassword.text.toString()==""){
            binding.textInputLayoutConfirmPassword.error="This is a required field! "
            binding.textInputLayoutConfirmPassword.errorIconDrawable=null

            return false
        }
        if(binding.etPassword.text.toString()!=binding.etConfirmPassword.text.toString()){

            binding.textInputLayoutPassword.error="Password do not match"
            return false
        }
        return true
    }
}