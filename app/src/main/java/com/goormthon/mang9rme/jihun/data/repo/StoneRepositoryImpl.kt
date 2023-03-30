package com.goormthon.mang9rme.jihun.data.repo

import android.util.Log
import com.goormthon.mang9rme.ApplicationClass
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.jihun.data.api.StoneService
import com.goormthon.mang9rme.jihun.data.data.StoneFeedResponse
import retrofit2.Response
import javax.inject.Inject

class StoneRepositoryImpl @Inject constructor(private val stoneService: StoneService) : StoneRepository{

    override suspend fun getStoneData(): Result<List<StoneData>> {
        val result = stoneService.getStoneFeedData()
        return if(result.isSuccessful) {
            Log.d("----", "getStoneData: ${result.body()?.data}")
            if(result.body()?.let { it.resultCode == ""} == true) {
                Result.success(result.body()!!.data)
            } else {
                Result.failure(IllegalArgumentException("서버 오류로 인해 응답이 오지 않았어요!"))
            }
        } else {
            Log.d("----", "getStoneData: FAIL")
            Result.failure(IllegalArgumentException("서버 연결에 실패했어요!"))
        }
    }
}