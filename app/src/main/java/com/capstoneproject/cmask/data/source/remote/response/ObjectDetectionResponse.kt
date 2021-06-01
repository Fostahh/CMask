package com.capstoneproject.cmask.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ObjectDetectionResponse(
    val url: String
) : Parcelable
