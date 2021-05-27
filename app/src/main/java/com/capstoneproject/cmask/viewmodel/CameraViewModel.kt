package com.capstoneproject.cmask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import com.capstoneproject.cmask.repository.ImageResultRepository
import okhttp3.MultipartBody

class CameraViewModel(private val imageResultRepository: ImageResultRepository): ViewModel() {
    fun uploadImage(body: MultipartBody.Part) : LiveData<UploadResponse> = imageResultRepository.uploadImage(body)
}