package com.example.freelanceflow.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.freelanceflow.R
import com.example.freelanceflow.databinding.ActivityMainBinding
import com.example.freelanceflow.databinding.ActivitySignUpBinding
import com.example.freelanceflow.repository.RemoteJobRepository
import com.example.freelanceflow.viewmodels.RemoteJobViewModel
import com.example.freelanceflow.viewmodels.RemoteJobViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var remoteJobViewModel: RemoteJobViewModel
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.jobFragment -> {
                val navController = findNavController(R.id.bottomNavView)
                navController.navigate(R.id.jobFragment)
                true
            }
            R.id.postsFragment -> {
                // Navigate to the ProfileFragment
                val navController = findNavController(R.id.bottomNavView)
                navController.navigate(R.id.postsFragment)
                true
            }
            R.id.techNewsFragment -> {
                // Navigate to the ProfileFragment
                val navController = findNavController(R.id.bottomNavView)
                navController.navigate(R.id.techNewsFragment)
                true
            }
            R.id.accountFragment -> {
                // Navigate to the ProfileFragment
                val navController = findNavController(R.id.bottomNavView)
                navController.navigate(R.id.accountFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
       setUpViewModel()

    }

    private fun setUpViewModel(){
      val remoteJobRepository = RemoteJobRepository()
        val remoteViewModelFactory = RemoteJobViewModelFactory(application, remoteJobRepository)

      remoteJobViewModel = ViewModelProvider(this, remoteViewModelFactory).get(RemoteJobViewModel::class.java)
}

}