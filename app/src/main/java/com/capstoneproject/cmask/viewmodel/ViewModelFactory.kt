package com.capstoneproject.cmask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.cmask.repository.ImageResultRepository
import com.capstoneproject.cmask.repository.RepositoryInterface

class ViewModelFactory(private val repositoryInterface: RepositoryInterface): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(CameraViewModel::class.java)
                    && repositoryInterface is ImageResultRepository -> {
                return CameraViewModel(repositoryInterface) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}