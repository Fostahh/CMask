package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.capstoneproject.cmask.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        mAuth = FirebaseAuth.getInstance()

        binding.buttonConfirm.setOnClickListener {
            val email = binding.editTextSignUpEmail.text.toString().trim()
            val password = binding.editTextSignUpPassword.text.toString().trim()
            val username = binding.editTextSignUpUsername.text.toString().trim()
            val phoneNumber = binding.editTextSignUpPhoneNumber.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all the blank box", Toast.LENGTH_SHORT).show()
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Account successfully created", Toast.LENGTH_SHORT).show()

                        val userId = mAuth.currentUser?.uid
                        userId?.let {
                            databaseReference =
                                FirebaseDatabase.getInstance().reference.child("Users").child(userId)
                            val user = HashMap<String, Any>()
                            user["username"] = username
                            user["email"] = email
                            user["phoneNumber"] = phoneNumber
                            databaseReference.setValue(user).addOnSuccessListener {
                            }
                        }

                        startActivity(Intent(this, SignInActivity::class.java))
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}