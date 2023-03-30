package com.goormthon.mang9rme.jihun.presentation.ui.main.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.jihun.data.repo.StoneRepository
import com.goormthon.mang9rme.jihun.data.repo.StoneRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val stoneRepo: StoneRepository) : ViewModel() {

    private val _stoneFeedData = MutableLiveData<MutableList<StoneData>>()
    val stoneFeedData: LiveData<MutableList<StoneData>> = _stoneFeedData

    private val _userToastMsg = MutableLiveData<String>()
    val userToastMsg: LiveData<String> = _userToastMsg

    private val _markerLocation = MutableLiveData<Pair<Double, Double>>()
    val markerLocation : LiveData<Pair<Double, Double>> = _markerLocation

    fun getStoneFeedData() {
        viewModelScope.launch {
            kotlin.runCatching {
                stoneRepo.getStoneData()
                    .onSuccess {
                        _stoneFeedData.value = it as MutableList
                    }
                    .onFailure {
                        _userToastMsg.value = it.message
                    }
            }.onFailure {
            }

        }
    }

    fun setLocation(lat : Double, lng : Double) {
        _markerLocation.value = Pair(lat, lng)
    }
}