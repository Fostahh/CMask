package com.capstoneproject.cmask.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadResponse(
    val keterangan: String?,
    val nilaiAkurat: Double?,
) : Parcelable