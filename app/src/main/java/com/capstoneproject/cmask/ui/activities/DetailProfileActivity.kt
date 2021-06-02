package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.cmask.databinding.ActivityDetailProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference.child("users/" + mAuth.currentUser?.uid + "/profile.jpg")

        binding.progressBar2.visibility = View.VISIBLE
        binding.linearLayoutVertical.visibility = View.GONE

        mAuth.currentUser?.let { firebaseUser ->
            databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

            storageReference.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(binding.circleImageViewDetailUserAvatar)
                binding.progressBar2.visibility = View.GONE
                binding.linearLayoutVertical.visibility = View.VISIBLE
            }

            databaseReference.get().addOnSuccessListener {
                binding.editTextDetailUserUsernameValue.setText(it.child("username").value.toString())
                binding.editTextDetailUserEmailValue.setText(it.child("email").value.toString())
                binding.editTextDetailUserPhonenumberValue.setText(it.child("phoneNumber").value.toString())
            }

        }

        binding.textViewDetailUserChangeAvatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, 100)
        }

        binding.buttonUpdateProfile.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val email = binding.editTextDetailUserEmailValue.text.toString().trim()
        val username = binding.editTextDetailUserUsernameValue.text.toString().trim()
        val phoneNumber = binding.editTextDetailUserPhonenumberValue.text.toString().trim()

        val userInfo = HashMap<String, Any>()
        userInfo["email"] = email
        userInfo["phoneNumber"] = phoneNumber
        userInfo["username"] = username

        databaseReference.updateChildren(userInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
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
            Picasso.get().load(imageUri).into(binding.circleImageViewDetailUserAvatar)
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