package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.capstoneproject.cmask.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Sign In"

        mAuth = FirebaseAuth.getInstance()

        binding.buttonSignInLogin.setOnClickListener {
            val email = binding.editTextSignInEmail.text.toString().trim()
            val password = binding.editTextSignInPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the blank box", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}