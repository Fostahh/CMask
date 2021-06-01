package com.capstoneproject.cmask.repository

import androidx.lifecycle.LiveData
import com.capstoneproject.cmask.data.source.DataSource
import com.capstoneproject.cmask.data.source.remote.response.ObjectDetectionResponse
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import okhttp3.MultipartBody

class ImageResultRepository(private val dataSource: DataSource) : RepositoryInterface {
    override fun uploadImage(body: MultipartBody.Part): LiveData<UploadResponse> =
        dataSource.uploadImage(body)

    override fun uploadImageObjectDetection(url: String, body: MultipartBody.Part): LiveData<ObjectDetectionResponse> =
        dataSource.uploadImageObjectDetection(url, body)

}