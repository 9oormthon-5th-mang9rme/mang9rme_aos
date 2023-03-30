package com.goormthon.mang9rme.jihun.data.repo

import com.goormthon.mang9rme.common.data.StoneData

interface StoneRepository {

    suspend fun getStoneFeedData() : Result<List<StoneData>>

    suspend fun getStoneData(stoneId : Int) : Result<StoneData>
}