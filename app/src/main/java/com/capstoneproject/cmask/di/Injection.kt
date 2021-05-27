package com.capstoneproject.cmask.di

import com.capstoneproject.cmask.data.source.remote.RemoteDataSource
import com.capstoneproject.cmask.repository.ImageResultRepository

object Injection {
    fun provideImageResultRepository(): ImageResultRepository {
        return ImageResultRepository(RemoteDataSource())
    }
}