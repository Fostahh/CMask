package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.capstoneproject.cmask.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Sign Up"

        mAuth = FirebaseAuth.getInstance()

        binding.buttonConfirmSignUp.setOnClickListener {
            val email = binding.editTextSignUpEmail.text.toString().trim()
            val password = binding.editTextSignUpPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the blank box", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Account successfully created", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignInActivity::class.java))
            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}