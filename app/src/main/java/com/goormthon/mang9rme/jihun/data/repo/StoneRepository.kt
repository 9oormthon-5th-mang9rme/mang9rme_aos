package com.goormthon.mang9rme.jihun.data.repo

import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.jihun.data.BaseResponse
import com.goormthon.mang9rme.jihun.data.data.StoneNameModifyRequest

interface StoneRepository {

    suspend fun getStoneFeedData() : Result<List<StoneData>>

    suspend fun getStoneData(stoneId : Int) : Result<StoneData>

    suspend fun patchStoneName(body : StoneNameModifyRequest) : Result<BaseResponse>
}