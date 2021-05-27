package com.capstoneproject.cmask.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.cmask.data.source.DataSource
import com.capstoneproject.cmask.data.source.remote.network.ApiConfig
import com.capstoneproject.cmask.data.source.remote.response.UploadResponse
import com.capstoneproject.cmask.utils.IdlingResource
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoteDataSource : DataSource {

    override fun uploadImage(body: MultipartBody.Part) : LiveData<UploadResponse>{
        val imageResult = MutableLiveData<UploadResponse>()

        IdlingResource.increment()
        ApiConfig.apiInstance.uploadImage(body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        imageResult.postValue(it)
                    }
                    IdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.d("onFailure", t.localizedMessage as String)
            }

        })

        return imageResult
    }
}