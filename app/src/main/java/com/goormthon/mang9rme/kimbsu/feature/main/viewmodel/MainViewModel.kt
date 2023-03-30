package com.goormthon.mang9rme.kimbsu.feature.main.viewmodel

import com.goormthon.mang9rme.kimbsu.feature.base.viewmodel.BaseViewModel
import com.goormthon.mang9rme.kimbsu.feature.main.repository.MainRepository

class MainViewModel(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    companion object {
        private const val TAG: String = "MainViewModel"
    }

}