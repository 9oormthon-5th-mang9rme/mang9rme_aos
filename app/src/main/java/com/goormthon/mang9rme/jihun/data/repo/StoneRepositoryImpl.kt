package com.goormthon.mang9rme.jihun.data.repo

import android.util.Log
import com.goormthon.mang9rme.ApplicationClass
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.jihun.data.BaseResponse
import com.goormthon.mang9rme.jihun.data.api.StoneService
import com.goormthon.mang9rme.jihun.data.data.StoneFeedResponse
import com.goormthon.mang9rme.jihun.data.data.StoneNameModifyRequest
import retrofit2.Response
import javax.inject.Inject

class StoneRepositoryImpl @Inject constructor(private val stoneService: StoneService) :
    StoneRepository {

    override suspend fun getStoneFeedData(): Result<List<StoneData>> {
        val result = stoneService.getStoneFeedData()
        return if (result.isSuccessful) {
            if (result.body()?.let { it.resultCode == "" } == true) {
                Result.success(result.body()!!.data)
            } else {
                Result.failure(IllegalArgumentException("서버 오류로 인해 응답이 오지 않았어요!"))
            }
        } else {
            Result.failure(IllegalArgumentException("서버 연결에 실패했어요!"))
        }
    }

    override suspend fun getStoneData(stoneId: Int): Result<StoneData> {
        val result = stoneService.getStoneData(stoneId)
        return if (result.isSuccessful) {
            if (result.body()?.let { it.resultCode == "" } == true) {
                Result.success(result.body()!!.data)
            } else {
                Result.failure(IllegalArgumentException("서버 오류로 인해 응답이 오지 않았어요!"))
            }
        } else {
            Result.failure(IllegalArgumentException("서버 연결에 실패했어요!"))
        }
    }

    override suspend fun patchStoneName(body: StoneNameModifyRequest): Result<BaseResponse> {
        val result = stoneService.patchStoneName(body)
        return if (result.isSuccessful) {
            if (result.body()?.resultCode == "") {
                Result.success(result.body()!!)
            } else {
                Result.failure(IllegalArgumentException("서버 오류로 인해 응답이 오지 않았어요!"))
            }
        } else {
            Result.failure(IllegalArgumentException("서버 연결에 실패했어요!"))
        }
    }
}
