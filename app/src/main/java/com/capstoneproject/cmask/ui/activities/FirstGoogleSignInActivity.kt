package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.capstoneproject.cmask.databinding.ActivityFirstGoogleSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class FirstGoogleSignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstGoogleSignInBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstGoogleSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        storageReference =
            FirebaseStorage.getInstance().reference.child("users/" + mAuth.currentUser?.uid + "/profile.jpg")
        mAuth.currentUser?.let {
            databaseReference =
                FirebaseDatabase.getInstance().reference.child("Users").child(it.uid)
        }

        binding.textViewFirstGoogleSignInChangeAvatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, 100)
        }

        binding.buttonConfirmGoogleSignIn.setOnClickListener {
            insertDataProfile()
        }
    }

    private fun insertDataProfile() {
        val username = binding.editTextFirstGoogleSignInUsernameValue.text.toString().trim()
        val phoneNumber = binding.editTextFirstGoogleSignInPhonenumberValue.text.toString().trim()

        val email: String? = mAuth.currentUser?.email

        val userInfo = HashMap<String, Any>()
        userInfo["email"] = email.toString()
        userInfo["username"] = username
        userInfo["phoneNumber"] = phoneNumber

        databaseReference.updateChildren(userInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Insert Data Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(
                    this,
                    "${it.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            Picasso.get().load(imageUri).into(binding.circleImageViewFirstGoogleSignInUserAvatar)
            uploadProfileImage(imageUri)
        }
    }

    private fun uploadProfileImage(imageUri: Uri?) {
        if (imageUri != null) {
            storageReference.putFile(imageUri).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}