package com.capstoneproject.cmask.repository

import androidx.lifecycle.LiveData
import com.capstoneproject.cmask.data.source.remote.response.ObjectDetectionResponse
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import okhttp3.MultipartBody

interface RepositoryInterface {
    fun uploadImage(body: MultipartBody.Part): LiveData<UploadResponse>
    fun uploadImageObjectDetection(url: String, body: MultipartBody.Part): LiveData<ObjectDetectionResponse>
}