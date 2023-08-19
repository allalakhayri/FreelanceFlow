package com.example.freelanceflow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.ActivityOnBoardingBinding
import com.example.freelanceflow.onboarding.OnBoardingItems
import com.example.freelanceflow.onboarding.OnBoardingItemsAdapter
import com.google.firebase.auth.FirebaseAuth


class OnBoardingActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var onBoardingBinding: ActivityOnBoardingBinding
    lateinit var adapter: OnBoardingItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBoardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        auth = FirebaseAuth.getInstance()
        setUpOnBoarding()
    }


    private fun setUpOnBoarding(){

        adapter = OnBoardingItemsAdapter(
            listOf(

                OnBoardingItems(
                    onBoardingImage = R.drawable.job,
                    title = "Find Interesting Projects",
                    description = "Discover a world of captivating projects \n  tailored to your passions and expertise."
                ),
                OnBoardingItems(
                    onBoardingImage = R.drawable.resume,
                    title = "Build your Resume",
                    description = "Craft a compelling resume by showcasing \n your contributions and accomplishments across the projects you engage with."
                ),
                OnBoardingItems(
                    onBoardingImage = R.drawable.blog,
                    title = "Write your Blogs ",
                    description = "Share your insights, experiences, and expertise \n by creating engaging blogs that inspire and inform."
                ),

                OnBoardingItems(
                    onBoardingImage = R.drawable.getstarted,
                    title = "Get Started",
                    description = "Let's begin this journey together . \n  Hit the ground running and start exploring our platform's endless possibilities."
                ),
            )
        )

        onBoardingBinding.onBoardingViewPager.adapter = adapter

        if (auth.currentUser!= null){
            redirect("MAIN")
        }

        onBoardingBinding.btnGetStarted.setOnClickListener {
            redirect("LOGIN")
        }
    }

    private fun redirect(status: String){
        val intent = when(status){
            "LOGIN" -> Intent(this, SignInActivity::class.java)
            "MAIN" -> Intent(this, MainActivity::class.java)
            else -> throw Exception("Failure!")
        }
        startActivity(intent)
        finish()
    }
}