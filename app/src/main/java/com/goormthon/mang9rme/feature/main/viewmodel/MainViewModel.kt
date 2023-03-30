package com.goormthon.mang9rme.feature.main.viewmodel

import com.goormthon.mang9rme.feature.base.viewmodel.BaseViewModel
import com.goormthon.mang9rme.feature.main.repository.MainRepository

class MainViewModel(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    companion object {
        private const val TAG: String = "MainViewModel"
    }

}