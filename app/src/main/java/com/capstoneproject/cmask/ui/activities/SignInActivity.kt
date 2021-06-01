package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.cmask.R
import com.capstoneproject.cmask.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class SignInActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val RC_SIGN_IN = 100
    }

    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.buttonSignIn.setOnClickListener(this)
        binding.textView3.setOnClickListener(this)
        binding.buttonGoogleSignIn.setOnClickListener(this)
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.let { firebaseUser ->
                        val databaseReference = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(firebaseUser.uid)
                        databaseReference.get().addOnSuccessListener {
                            if(it.child("username").value == null) {
                                startActivity (Intent(this, FirstGoogleSignInActivity::class.java))
                            } else {
                                startActivity (Intent(this, HomeActivity::class.java))
                            }
                        }

                    }
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSignIn -> {
                val email = binding.editTextSignInEmail.text.toString().trim()
                val password = binding.editTextSignInPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please fill all the blank box", Toast.LENGTH_SHORT).show()
                } else {
                    loginUser(email, password)
                }
            }
            R.id.textView3 -> startActivity(Intent(this, SignUpActivity::class.java))
            R.id.buttonGoogleSignIn -> signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}