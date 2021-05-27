package com.capstoneproject.cmask.data.source

import androidx.lifecycle.LiveData
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import okhttp3.MultipartBody

interface DataSource {
    fun uploadImage(body: MultipartBody.Part): LiveData<UploadResponse>
}