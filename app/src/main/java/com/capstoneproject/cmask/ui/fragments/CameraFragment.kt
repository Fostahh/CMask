package com.capstoneproject.cmask.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.cmask.databinding.FragmentCameraBinding
import com.capstoneproject.cmask.di.Injection
import com.capstoneproject.cmask.ui.activities.CameraResultActivity
import com.capstoneproject.cmask.viewmodel.CameraViewModel
import com.capstoneproject.cmask.viewmodel.ViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class CameraFragment : Fragment() {

    companion object {
        const val REQUEST_CAMERA = 100
    }

    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intentCamera()
    }

    private fun intentCamera() {
        Dexter.withContext(activity).withPermissions(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                if (p0!!.areAllPermissionsGranted()) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CAMERA)
                } else {
                    Toast.makeText(
                        activity,
                        "You must accept all the permission to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {

            }

        }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            convertFileToMultipartBody("${UUID.randomUUID()}.jpg", imageBitmap)
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun convertBitmapToFile(fileName: String, imageBitmap: Bitmap): File {
        val file = File(context?.cacheDir, fileName)
        file.createNewFile()

        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val fos = FileOutputStream(file)
        fos.write(image)
        fos.flush()
        fos.close()

        return file
    }

    private fun convertFileToMultipartBody(fileName: String, imageBitmap: Bitmap) {
        val leftImageFile = convertBitmapToFile(fileName, imageBitmap)
        val reqFile = leftImageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", leftImageFile.name, reqFile)

        val factory = ViewModelFactory(Injection.provideImageResultRepository())
        val viewModel = ViewModelProvider(this, factory)[CameraViewModel::class.java]
        viewModel.uploadImage(body).observe(viewLifecycleOwner, {
            val intent = Intent(context, CameraResultActivity::class.java)
            intent.putExtra("image", imageBitmap)
            intent.putExtra(CameraResultActivity.DATA_IMAGE_RESULT, it)
            startActivity(intent)
        })
    }
}