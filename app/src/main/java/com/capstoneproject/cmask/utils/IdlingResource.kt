package com.capstoneproject.cmask.utils

import androidx.test.espresso.idling.CountingIdlingResource

object IdlingResource {

    private const val RESOURCE = "GLOBAL"

    private val idlingResource = CountingIdlingResource(RESOURCE)


    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        idlingResource.decrement()
    }

}