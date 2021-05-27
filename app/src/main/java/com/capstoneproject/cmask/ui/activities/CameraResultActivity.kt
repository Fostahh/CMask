package com.capstoneproject.cmask.ui.activities

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import com.capstoneproject.cmask.databinding.ActivityCameraResultBinding


class CameraResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraResultBinding

    companion object {
        const val DATA_IMAGE_RESULT = "image_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uploadResponse : UploadResponse? = intent.getParcelableExtra(DATA_IMAGE_RESULT)
        val imageCaptured : Bitmap? = intent.getParcelableExtra("image")

        binding.imageViewResult.setImageBitmap(imageCaptured)
        uploadResponse?.let {
            binding.textViewImageResultKeterangan.text = it.keterangan
            binding.textViewImageResultAccuracy.text = "${it.nilaiAkurat}"
        }

//        val imageResult = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")
//        imageResult.downloadUrl.addOnSuccessListener {
//            Picasso.get().load(it).into(binding.imageViewResult)
//        }

    }
}