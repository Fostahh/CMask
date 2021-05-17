package com.capstoneproject.cmask.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.cmask.databinding.ActivityCameraResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class CameraResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageResult = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")
        imageResult.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(binding.imageViewResult)
        }
    }
}