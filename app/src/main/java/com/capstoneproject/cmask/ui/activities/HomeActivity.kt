package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstoneproject.cmask.R
import com.capstoneproject.cmask.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"

        mAuth = FirebaseAuth.getInstance()

        val navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.navigation_home,
                R.id.navigation_camera,
                R.id.navigation_profile
            )
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                mAuth.signOut()
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}