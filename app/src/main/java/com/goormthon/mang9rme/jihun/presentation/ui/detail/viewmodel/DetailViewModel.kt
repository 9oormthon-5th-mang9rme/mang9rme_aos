package com.goormthon.mang9rme.jihun.presentation.ui.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.jihun.data.data.StoneNameModifyRequest
import com.goormthon.mang9rme.jihun.data.repo.StoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val stoneRepository: StoneRepository): ViewModel() {

    private val _stoneData = MutableLiveData<StoneData>()
    val stoneData : LiveData<StoneData> = _stoneData

    private val _userToastMsg = MutableLiveData<String>()
    val userToastMsg: LiveData<String> = _userToastMsg

    fun getStoneData(stoneId : Int) {
        viewModelScope.launch {
            stoneRepository.getStoneData(stoneId)
                .onSuccess {
                    _stoneData.value = it }
                .onFailure {
                    _userToastMsg.value = it.message }
        }
    }

    fun patchStoneName(modifiedName : String) {
        viewModelScope.launch {
            stoneRepository.patchStoneName(StoneNameModifyRequest(stoneData.value!!.stoneId, modifiedName))
                .onSuccess { _userToastMsg.value = "이름 변경에 성공했습니다!" }
                .onFailure { _userToastMsg.value = it.message }
        }
    }
}