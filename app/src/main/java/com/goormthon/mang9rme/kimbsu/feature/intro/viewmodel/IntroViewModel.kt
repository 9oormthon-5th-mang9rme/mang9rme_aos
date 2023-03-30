package com.goormthon.mang9rme.kimbsu.feature.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.data.network.NetworkResult
import com.goormthon.mang9rme.kimbsu.feature.base.viewmodel.BaseViewModel
import com.goormthon.mang9rme.kimbsu.feature.intro.repository.IntroRepository
import kotlinx.coroutines.launch

class IntroViewModel(
    private val repository: IntroRepository
) : BaseViewModel() {

    /**
     * 돌 데이터 리스트
     */
    private val _stoneDataList: MutableLiveData<ArrayList<StoneData>> = MutableLiveData()
    val stoneDataList: LiveData<ArrayList<StoneData>> get() = _stoneDataList

    /**
     * 돌 데이터 리스트를 요청하는 API를 호출하는 함수
     */
    fun requestStoneDataList() {
        viewModelScope.launch {
            setProgressFlag(true)
            when (val result = repository.makeStoneDataListRequest()) {
                is NetworkResult.Success<ArrayList<StoneData>> -> {
                    val data = result.data
                    DLog.d("${TAG}_requestStoneDataList", "stoneDataList=$data")

                    _stoneDataList.value = data
                }
                is NetworkResult.Fail -> {
                    val failMsg = result.msg
                    DLog.e("${TAG}_requestStoneDataList", failMsg)
                    setApiFailMsg(failMsg)
                }
                is NetworkResult.Error -> {
                    val exception = result.exception
                    DLog.e(
                        "${TAG}_requestStoneDataList",
                        "exception=${exception.message}",
                        exception
                    )
                    setExceptionData(exception)
                }
            }
        }
    }

    companion object {
        private const val TAG: String = "IntroViewModel"
    }

}