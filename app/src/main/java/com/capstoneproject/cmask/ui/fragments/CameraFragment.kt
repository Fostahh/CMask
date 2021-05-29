package com.capstoneproject.cmask.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.cmask.BuildConfig
import com.capstoneproject.cmask.databinding.FragmentCameraBinding
import com.capstoneproject.cmask.di.Injection
import com.capstoneproject.cmask.ui.activities.CameraResultActivity
import com.capstoneproject.cmask.viewmodel.CameraViewModel
import com.capstoneproject.cmask.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : Fragment() {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var binding: FragmentCameraBinding
    private var photoFile: File? = null
    private var decision: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonKlasifikasi.setOnClickListener {
            decision = 1
            dispatchTakePictureIntent()
        }

        binding.imageButtonObjectDetection.setOnClickListener {
            decision = 2
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    photoFile = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        context?.let { context ->
                            val photoURI: Uri = FileProvider.getUriForFile(
                                context,
                                "com.capstoneproject.cmask.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            binding.progressBar.visibility = View.VISIBLE
            val imageBitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)
            val reducedImageBitmap = getResizedBitmap(imageBitmap)
            val convertedFile = convertBitmapToFile(reducedImageBitmap)
            convertedFile?.let {
                convertFileToMultipartBody(it)
            }
        }
    }

    private fun getResizedBitmap(image: Bitmap): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = 1000
            height = (width / bitmapRatio).toInt()
        } else {
            height = 1000
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File? {
        photoFile?.also {
            it.delete()
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val image = baos.toByteArray()

            val fos = FileOutputStream(it)
            fos.write(image)
            fos.flush()
            fos.close()

            return it
        }

        return null
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun convertFileToMultipartBody(file: File) {
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", file.name, reqFile)

        val factory = ViewModelFactory(Injection.provideImageResultRepository())
        val viewModel = ViewModelProvider(this, factory)[CameraViewModel::class.java]
        if (decision == 1) {
            viewModel.uploadImage(body).observe(viewLifecycleOwner, {
                val intent = Intent(context, CameraResultActivity::class.java)
                intent.putExtra(CameraResultActivity.IMAGE_RESULT, photoFile?.absolutePath)
                intent.putExtra(CameraResultActivity.RESPONSE_RESULT, it)
                intent.putExtra(CameraResultActivity.DECISION, decision)
                startActivity(intent)
            })
        } else if (decision == 2) {
            viewModel.uploadImageObjectDetection(BuildConfig.ObjectDetectionURL, body)
                .observe(viewLifecycleOwner, {
                    val intent = Intent(context, CameraResultActivity::class.java)
                    intent.putExtra(CameraResultActivity.RESPONSE_RESULT, it.url)
                    intent.putExtra(CameraResultActivity.DECISION, decision)
                    startActivity(intent)
                })
        }

    }
}