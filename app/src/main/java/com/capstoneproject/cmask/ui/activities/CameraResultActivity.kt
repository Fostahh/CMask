package com.capstoneproject.cmask.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.cmask.R
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import com.capstoneproject.cmask.databinding.ActivityCameraResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class CameraResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraResultBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference1: DatabaseReference
    private lateinit var databaseReference2: DatabaseReference
    private lateinit var timeStamp: String
    private lateinit var bitmap: Bitmap

    companion object {
        const val RESPONSE_RESULT = "response_result"
        const val IMAGE_RESULT = "image_result"
        const val DECISION = "decision"
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.uid?.let {
            databaseReference1 =
                FirebaseDatabase.getInstance().reference.child("Users").child(it).child("History/Classification")
                    .child(timeStamp)
            databaseReference2 =
                FirebaseDatabase.getInstance().reference.child("Users").child(it).child("History/ObjectDetection")
                    .child(timeStamp)
        }

        val decision = intent.getIntExtra(DECISION, 0)
        if (decision == 1) {
            bitmap = BitmapFactory.decodeFile(intent.getStringExtra(IMAGE_RESULT))
            val uploadResponse: UploadResponse? = intent.getParcelableExtra(RESPONSE_RESULT)
            binding.imageViewResult.setImageBitmap(bitmap)
            uploadResponse?.let {
                if (it.keterangan == "Tidak Menggunakan") {
                    binding.linearLayoutObjectDetection.visibility = View.GONE
                    binding.textViewImageResultKeterangan.text =
                        resources.getString(R.string.keterangan, it.keterangan)
                    binding.textViewImageResultAccuracy.text = "Akurasi: ${it.nilaiAkurat}"
                    binding.imageViewIncorrect.visibility = View.VISIBLE
                } else {
                    binding.textViewImageResultKeterangan.text =
                        resources.getString(R.string.keterangan, it.keterangan)
                    binding.textViewImageResultAccuracy.text = "Akurasi: ${it.nilaiAkurat}"
                    binding.imageViewCorrect.visibility = View.VISIBLE
                }
                it.keterangan?.let { keterangan ->
                    it.nilaiAkurat?.let { nilaiAkurat ->
                        uploadResponseClassificationToFirebase(keterangan, nilaiAkurat)
                    }
                }
            }
        } else if (decision == 2) {
            binding.linearLayoutKlasifikasi.visibility = View.GONE
            val imageUrl: String? = intent.getStringExtra(RESPONSE_RESULT)
            Picasso.get().load(imageUrl).into(binding.imageViewResult)
            uploadResponseObjectDetectionToFirebase(imageUrl)
        }
    }

    private fun uploadResponseObjectDetectionToFirebase(imageUrl: String?) {
        val historyInfo = HashMap<String, Any>()
        historyInfo["photoUrl"] = imageUrl.toString()
        historyInfo["date"] = getCurrentDate()
        historyInfo["time"] = getCurrentTime()

        databaseReference2.updateChildren(historyInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadResponseClassificationToFirebase(keterangan: String, nilaiAkurat: Double) {
        val historyInfo = HashMap<String, Any>()
        historyInfo["keterangan"] = keterangan
        historyInfo["nilaiAkurat"] = nilaiAkurat
        historyInfo["date"] = getCurrentDate()
        historyInfo["time"] = getCurrentTime()

        databaseReference1.updateChildren(historyInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                uploadImageToFirebase()
            } else {
                Toast.makeText(this, "${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToFirebase() {
        val storageReference =
            FirebaseStorage.getInstance().reference.child("users/" + mAuth.currentUser?.uid + "/history/" + timeStamp + ".jpg")
        val byte = convertBitmapToByte(bitmap)
        storageReference.putBytes(byte).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertBitmapToByte(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)

        return baos.toByteArray()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val time = Date()
        return timeFormat.format(time)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }
}