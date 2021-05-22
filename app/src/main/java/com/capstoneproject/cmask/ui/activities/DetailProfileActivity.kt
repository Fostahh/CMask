package com.capstoneproject.cmask.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstoneproject.cmask.databinding.ActivityDetailProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.let { firebaseUser ->
            databaseReference =
                FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

            databaseReference.get().addOnSuccessListener {
                binding.textViewDetailUserNameValue.text = it.child("username").value.toString()
                binding.textViewDetailUserEmailValue.text = it.child("email").value.toString()
                binding.textViewDetailUserPhoneNumberValue.text =
                    it.child("phoneNumber").value.toString()
            }
        }

        binding.textViewDetailUserChangeAvatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            binding.circleImageViewDetailUserAvatar.setImageURI(imageUri)
        }
    }

}