package com.goormthon.mang9rme.feature.main.repository

import android.app.Application
import com.goormthon.mang9rme.feature.base.repository.BaseNetworkRepository

class MainRepository(
    private val application: Application
) : BaseNetworkRepository(application, TAG) {

    companion object {
        private const val TAG: String = "MainRepository"
    }

}