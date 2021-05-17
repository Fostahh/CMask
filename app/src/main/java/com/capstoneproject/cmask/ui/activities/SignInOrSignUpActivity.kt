package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstoneproject.cmask.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class SignInOrSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Main"

        mAuth = FirebaseAuth.getInstance()

        binding.buttonSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}