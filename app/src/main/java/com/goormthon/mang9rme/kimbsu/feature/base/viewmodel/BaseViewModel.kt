package com.goormthon.mang9rme.kimbsu.feature.base.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseViewModel : ViewModel() {

    /**
     * ProgressBar on/off flag
     */
    private val _progressFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressFlag: LiveData<Boolean> get() = _progressFlag

    /**
     * Exception Data
     *
     * Exception이 발생한 경우, 일시적인 오류임을 알리는 Toast 메시지를 출력함
     */
    private val _exceptionData: MutableLiveData<Exception> = MutableLiveData()
    val exceptionData: LiveData<Exception> get() = _exceptionData

    /**
     * API Fail Message
     *
     * API Request 실패 시, Fail Message를 Toast 메시지로 출력해야 함
     */
    private val _apiFailMsg: MutableLiveData<String> = MutableLiveData()
    val apiFailMsg: LiveData<String> get() = _apiFailMsg

    fun setProgressFlag(pProgressFlag: Boolean) {
        if (progressFlag.value != pProgressFlag)
            _progressFlag.value = pProgressFlag
    }

    fun setExceptionData(pException: Exception) {
        _exceptionData.value = pException
    }

    fun setApiFailMsg(pMsg: String) {
        _apiFailMsg.value = pMsg
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // MainActivity ViewModel
//            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//                return MainViewModel(MainRepository(application)) as T
//            }
            // 식별되지 않은 ViewModel
//            else {
                throw IllegalArgumentException("Unknown ViewModel Class!")
//            }
        }
    }


    companion object {
        private const val TAG: String = "BaseViewModel"
    }

}