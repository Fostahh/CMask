package com.capstoneproject.cmask.data.source.remote.network

import com.capstoneproject.cmask.data.source.remote.response.ObjectDetectionResponse
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("klasifikasi")
    fun uploadImage(
        @Part photo: MultipartBody.Part
    ): Call<UploadResponse>

    @Multipart
    @POST
    fun uploadImageObjectDetection(
        @Url url: String,
        @Part photo: MultipartBody.Part
    ): Call<ObjectDetectionResponse>
}