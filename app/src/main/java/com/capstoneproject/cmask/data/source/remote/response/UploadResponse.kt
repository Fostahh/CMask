package com.capstoneproject.cmask.data.source.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UploadResponse(
    val keterangan: String?,
    val nilaiAkurat: Double?,
) : Parcelable